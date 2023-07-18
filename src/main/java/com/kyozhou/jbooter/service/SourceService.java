package com.kyozhou.jbooter.service;

import com.kyozhou.jbooter.config.HttpException;
import com.kyozhou.jbooter.service.po.SourcePo;

import java.util.List;

public interface SourceService {

    SourcePo register(String orgUUID, String sourceUUID, Integer timezone) throws HttpException;
    List<SourcePo> getSourceList(String orgUUID);
}
