package com.kyozhou.jbooter.service.impl;

import com.kyozhou.jbooter.service.SourceService;
import com.kyozhou.jbooter.system.utils.CommonUtility;
import com.kyozhou.jbooter.config.HttpException;
import com.kyozhou.jbooter.service.dao.SourceDao;
import com.kyozhou.jbooter.service.po.SourcePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceServiceImpl implements SourceService {

    @Autowired
    private SourceDao sourceDao;

    @Override
    public SourcePo register(String orgUUID, String sourceUUID, Integer timezone) throws HttpException {
        SourcePo sourcePo = sourceDao.getSource(orgUUID, sourceUUID);
        if (sourcePo == null) {
            sourcePo = new SourcePo();
            sourceDao.register(sourceUUID, orgUUID, timezone);
            sourcePo.setUUID(sourceUUID);
            sourcePo.setOrgUUID(orgUUID);
            sourcePo.setTimeCreated(CommonUtility.getCurrentTimestamp());
        } else {
            throw new HttpException("source_is_registered");
        }
        return sourcePo;
    }

    @Override
    public List<SourcePo> getSourceList(String orgUUID) {
        return sourceDao.getSourceList(orgUUID);
    }
}
