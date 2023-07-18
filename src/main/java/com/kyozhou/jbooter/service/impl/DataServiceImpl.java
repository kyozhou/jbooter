package com.kyozhou.jbooter.service.impl;

import com.kyozhou.jbooter.service.po.SourcePo;
import com.kyozhou.jbooter.service.po.StandardRawDataPo;
import com.kyozhou.jbooter.system.utils.CommonUtility;
import com.kyozhou.jbooter.config.HttpException;
import com.kyozhou.jbooter.service.DataService;
import com.kyozhou.jbooter.service.dao.DataDao;
import com.kyozhou.jbooter.service.dao.SourceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private DataDao dataDao;
    @Autowired
    private SourceDao sourceDao;

    @Override
    public List<StandardRawDataPo> getStandardRawDataByDay(String orgUUID, String sourceUUID, String day) throws HttpException {
        SourcePo sourcePo = sourceDao.getSource(orgUUID, sourceUUID);
        if (sourcePo == null) {
            throw new HttpException("source_not_exists");
        }
        LocalDateTime localDateTime = LocalDateTime.parse(day + " 18:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Long timeStart = localDateTime.toEpochSecond(ZoneOffset.ofHours(sourcePo.getTimezone()));
        Long timeEnd = timeStart + 86400;
        List<StandardRawDataPo> standardRawDataPoList = dataDao.getStandardRawData(orgUUID, sourceUUID, timeStart, timeEnd);
        return standardRawDataPoList;
    }

    @Override
    public void addStandardRawDataMulti(List<StandardRawDataPo> standardRawDataPos, String orgUUID) throws HttpException {
        List<SourcePo> sourceList = sourceDao.getSourceList(orgUUID);
        Set<String> sourceUUIDSetAllow = new HashSet<>();
        for (SourcePo sourcePo : sourceList) {
            sourceUUIDSetAllow.add(sourcePo.getUUID());
        }
        Long timeNow = CommonUtility.getCurrentTimestamp();
        for (StandardRawDataPo standardRawDataPo : standardRawDataPos) {
            if (!sourceUUIDSetAllow.contains(standardRawDataPo.getSourceUUID())) {
                throw new HttpException("data_invalid");
            }
            if (standardRawDataPo.getTimestamp() > timeNow + 60 || standardRawDataPo.getTimestamp() < timeNow - 86400 * 7 - 3600) {
                throw new HttpException("data_invalid");
            }
            if (standardRawDataPo.getBreathRate() == null || standardRawDataPo.getBreathRate() < 0 || standardRawDataPo.getBreathRate() > 255) {
                throw new HttpException("data_invalid");
            }
            if (standardRawDataPo.getHeartRate() == null || standardRawDataPo.getHeartRate() < 0 || standardRawDataPo.getHeartRate() > 255) {
                throw new HttpException("data_invalid");
            }
            if (standardRawDataPo.getGatem() == null || standardRawDataPo.getGatem() < 0 || standardRawDataPo.getGatem() > 255) {
                throw new HttpException("data_invalid");
            }
            if (standardRawDataPo.getGateo() == null || standardRawDataPo.getGateo() < 0 || standardRawDataPo.getGateo() > 65535) {
                throw new HttpException("data_invalid");
            }
        }
        dataDao.insertMulti(standardRawDataPos);
    }

    @Override
    public void clearStandardRawData() {
        dataDao.clearStandardRawData();
    }


}
