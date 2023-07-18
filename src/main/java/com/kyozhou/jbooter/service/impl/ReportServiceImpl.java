package com.kyozhou.jbooter.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kyozhou.jbooter.service.dao.*;
import com.kyozhou.jbooter.service.po.SourcePo;
import com.kyozhou.jbooter.service.po.StandardRawDataPo;
import com.kyozhou.jbooter.system.aop.LoggerInterceptor;
import com.kyozhou.jbooter.system.utils.CommonUtility;
import com.kyozhou.jbooter.config.HttpException;
import com.kyozhou.jbooter.service.ReportService;
import com.mirahome.microservice.ms_report.service.dao.*;
import com.mongodb.lang.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);
    @Value("${beacon.url}")
    private String beaconUrl;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private DataDao dataDao;
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private SourceDao sourceDao;

    @Override
    public ObjectNode getBeacon2(String orgUUID, String inputJsonString) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode beacon2Report = null;
        if (inputJsonString != null && inputJsonString.length() > 100) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> entity = new HttpEntity<String>(inputJsonString, headers);
            try {
                beacon2Report = restTemplate.postForObject(beaconUrl + "/beacon2", entity, ObjectNode.class);
            } catch (Exception e) {
                logger.error(e.getMessage());
                logger.error("beacon2 error, input string is:" + inputJsonString);
                try {
                    logger.error("beacon2 error, output string is:" + objectMapper.writeValueAsString(beacon2Report));
                } catch (JsonProcessingException ex) {
                    ex.printStackTrace();
                }
            }
            if (this.isReportValid(beacon2Report)) {
                requestDao.countRequest(orgUUID, "beacon2", CommonUtility.getDayToday());
            }
        }
        return beacon2Report;
    }

    @Override
    public ObjectNode getBeacon2BySourceUUID(String orgUUID, String sourceUUID, String day, Boolean noCache) throws HttpException {
        //48小时的数据直接拿cache，拿不到返回{}
        ObjectMapper objectMapper = new ObjectMapper();
        LocalDate localDateParam = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate localDateNow = LocalDate.now();
        if (localDateNow.compareTo(localDateParam) >= 8 && !noCache) {
            ObjectNode reportCached = reportDao.getBeacon2(sourceUUID, day);
            if(this.isReportValid(reportCached)) {
                return reportCached;
            }
        } else {
            SourcePo sourcePo = sourceDao.getSource(orgUUID, sourceUUID);
            if (sourcePo == null) {
                throw new HttpException("source_not_exists");
            }
            LocalDateTime localDateTime = LocalDateTime.parse(day + " 18:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Long timeStart = localDateTime.toEpochSecond(ZoneOffset.ofHours(sourcePo.getTimezone()));
            Long timeEnd = timeStart + 86400;
            List<StandardRawDataPo> standardRawDataPoList = dataDao.getStandardRawData(orgUUID, sourceUUID, timeStart, timeEnd);
            if (standardRawDataPoList.size() > 10) {
                try {
                    String dataJson = objectMapper.writeValueAsString(standardRawDataPoList);
                    dataJson = "{\"config\": {\"timezone\":" + sourcePo.getTimezone() + "}, \"input_data\": " + dataJson + "}";
                    ObjectNode beacon2Report = this.getBeacon2(orgUUID, dataJson);
                    if (isReportValid(beacon2Report)) {
                        beacon2Report.put("org_uuid", orgUUID);
                        beacon2Report.put("source_uuid", sourceUUID);
                        logger.info("source_uuid: " + sourceUUID + ", day:" + day + ", start:" + timeStart + ", end:" + timeEnd +
                                ", first_raw_data_time:" + standardRawDataPoList.get(0).getTimestamp() +
                                ", beacon2->report_date:" + beacon2Report.get("report_date") + ", score: " + beacon2Report.get("score"));
                    }
                    return beacon2Report;
                } catch (JsonProcessingException e) {
                    throw new HttpException("json_parse_error");
                }
            }
        }
        return null;
    }

    @Override
    public List<ObjectNode> getBeacon2ByMonth(String orgUUID, String sourceUUID, String month) throws HttpException {
        YearMonth yearMonth = YearMonth.parse(month);
        String startDay = month + "-01";
        String endDay = yearMonth.atEndOfMonth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return this.getBeacon2Periodic(orgUUID, sourceUUID, startDay, endDay);
    }

    @Override
    public List<ObjectNode> getBeacon2Periodic(String orgUUID, String sourceUUID, String startDay, String endDay) throws HttpException {
        LinkedList<String> dayList = CommonUtility.getDayListByRange(startDay, endDay);
        if (dayList.size() > 60) {
            throw new HttpException("out_of_day_range");
        }
        List<ObjectNode> reportList = new LinkedList<>();
        for (String day : dayList) {
            ObjectNode beacon2Report = this.getBeacon2BySourceUUID(orgUUID, sourceUUID, day, false);
            if (isReportValid(beacon2Report)) {
                reportList.add(beacon2Report);
            }
        }
        return reportList;
    }

    @Override
    public void buildReport() throws HttpException {
        //拉取所有source，遍历生成昨日报告，并生成缓存
        List<String> orgUUIDList = organizationDao.getAllOrganizationUUID();
        List<SourcePo> sourcePoList = new ArrayList<>();
        List<ObjectNode> beacon2ReportListToBeSaved = new ArrayList<>();
        for(String orgUUID : orgUUIDList) {
            sourcePoList.addAll(sourceDao.getSourceList(orgUUID));
        }
        for(SourcePo sourcePo : sourcePoList) {
            Integer timezone = sourcePo.getTimezone();
            String day = this.getTargetDayByOffset(timezone);
            if(day != null) {
                ObjectNode beacon2Report = this.getBeacon2BySourceUUID(sourcePo.getOrgUUID(), sourcePo.getUUID(), day, true);
                if(isReportValid(beacon2Report)) {
                    beacon2ReportListToBeSaved.add(beacon2Report);
                }
            }
        }
        reportDao.saveBeacon2Report(beacon2ReportListToBeSaved);
    }

    @Override
    public void refreshCache(String month) {
        List<String> orgUUIDList = organizationDao.getAllOrganizationUUID();
        List<SourcePo> sourcePoList = new ArrayList<>();
        List<ObjectNode> beacon2ReportListToBeSaved = new ArrayList<>();
        for(String orgUUID : orgUUIDList) {
            sourcePoList.addAll(sourceDao.getSourceList(orgUUID));
        }
        for(SourcePo sourcePo : sourcePoList) {
            Integer timezone = sourcePo.getTimezone();
            YearMonth yearMonth = YearMonth.parse(month);
            LinkedList<String> dayList = CommonUtility.getDayListByRange(
                    month + "-01",
                    yearMonth.atEndOfMonth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            );
            for(String day : dayList) {
                ObjectNode beacon2Report = null;
                try {
                    beacon2Report = this.getBeacon2BySourceUUID(sourcePo.getOrgUUID(), sourcePo.getUUID(), day, true);
                } catch (HttpException e) {
                    e.printStackTrace();
                }
                if(this.isReportValid(beacon2Report)) {
                    beacon2ReportListToBeSaved.add(beacon2Report);
                }
            }
        }
        reportDao.saveBeacon2Report(beacon2ReportListToBeSaved);
    }

    private String getTargetDayByOffset(Integer timezoneOffset) {
        OffsetDateTime offsetDateTime = OffsetDateTime.now(ZoneOffset.ofHours(timezoneOffset));
        if(offsetDateTime.getHour() == 18) {
            return offsetDateTime.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else {
            return null;
        }
    }

    private Boolean isReportValid(JsonNode jsonNodeReport) {
        if(jsonNodeReport != null && jsonNodeReport.has("score") && jsonNodeReport.get("score").asInt() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
