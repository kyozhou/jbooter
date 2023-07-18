package com.kyozhou.jbooter.service;

import com.kyozhou.jbooter.service.po.StandardRawDataPo;
import com.kyozhou.jbooter.config.HttpException;

import java.util.List;

public interface DataService {

    List<StandardRawDataPo> getStandardRawDataByDay(String orgUUID, String sourceUUID, String day) throws HttpException;
    void addStandardRawDataMulti(List<StandardRawDataPo> standardRawDataPos, String orgUUID) throws HttpException;
    void clearStandardRawData();
}
