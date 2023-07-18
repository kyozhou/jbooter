package com.kyozhou.jbooter.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kyozhou.jbooter.system.constant.RegexConst;
import com.kyozhou.jbooter.config.HttpException;
import com.kyozhou.jbooter.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/report/beacon2")
    public ObjectNode getBeacon2(
//            @RequestParam(value = "org_uuid") String orgUUID,
            @RequestAttribute("_org_uuid") String orgUUID,
            @RequestBody String standardDataString
    ) {
        return reportService.getBeacon2(orgUUID, standardDataString);
    }

    @RequestMapping(value = "/report/beacon2/{source_uuid}")
    public ObjectNode getBeacon2BySource(
            @RequestAttribute("_org_uuid") String orgUUID,
            @PathVariable("source_uuid") String resourceUUID,
            @Pattern(regexp = RegexConst.DATE, message = "param_pattern_error") @RequestParam("day") String day,
            @RequestParam(value = "no_cache", required = false, defaultValue = "false") Boolean noCache
    ) throws HttpException {
        return reportService.getBeacon2BySourceUUID(orgUUID, resourceUUID, day, noCache);
    }

    @RequestMapping(value = "/report/beacon2-month-list/{source_uuid}")
    public List<ObjectNode> getBeacon2MonthList(
            @RequestAttribute("_org_uuid") String orgUUID,
            @PathVariable("source_uuid") String sourceUUID,
            @Pattern(regexp = RegexConst.MONTH, message = "param_pattern_error") @RequestParam("month") String month
    ) throws HttpException {
        return reportService.getBeacon2ByMonth(orgUUID, sourceUUID, month);
    }

    @RequestMapping(value = "/report/beacon2-periodic/{source_uuid}")
    public List<ObjectNode> getBeacon2Periodic(
            @RequestAttribute("_org_uuid") String orgUUID,
            @PathVariable("source_uuid") String sourceUUID,
            @Pattern(regexp = RegexConst.DATE, message = "param_pattern_error") @RequestParam("start_day") String startDay,
            @Pattern(regexp = RegexConst.DATE, message = "param_pattern_error") @RequestParam("end_day") String endDay
    ) throws HttpException {
        return reportService.getBeacon2Periodic(orgUUID, sourceUUID, startDay, endDay);
    }

    @RequestMapping(value = "/report/refresh-cache")
    public void refreshCache(@RequestParam("month") String month) {
        reportService.refreshCache(month);
    }

    @RequestMapping(value = "/report/test")
    public void test(@RequestParam(value = "arg", required = false) String arg) throws HttpException {

        reportService.buildReport();
    }
}
