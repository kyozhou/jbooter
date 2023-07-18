package com.kyozhou.jbooter.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kyozhou.jbooter.config.HttpException;

import java.util.List;

public interface ReportService {

    ObjectNode getBeacon2(String orgUUID, String inputJsonString);
    ObjectNode getBeacon2BySourceUUID(String orgUUID, String sourceUUID, String day, Boolean noCache) throws HttpException;
    List<ObjectNode> getBeacon2ByMonth(String orgUUID, String sourceUUID, String month) throws HttpException;
    List<ObjectNode> getBeacon2Periodic(String orgUUID, String sourceUUID, String startDay, String endDay) throws HttpException;
    void buildReport() throws HttpException;
    void refreshCache(String month);
}
