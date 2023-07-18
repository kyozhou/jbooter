package com.kyozhou.jbooter.service.impl;

import com.kyozhou.jbooter.system.utils.CommonUtility;
import com.kyozhou.jbooter.system.config.HttpException;
import com.kyozhou.jbooter.dao.OrganizationDao;
import com.kyozhou.jbooter.pojo.po.PermissionEnum;
import com.kyozhou.jbooter.pojo.po.TokenPo;
import com.kyozhou.jbooter.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationDao organizationDao;

    @Override
    public TokenPo generateToken(String accessKey, String clientUUID, String service, Long timestamp, String sign) throws HttpException {
        String secretKey = organizationDao.getSecretKeyByAccessKey(accessKey);
        String stringToSign = accessKey + clientUUID + service + timestamp.toString() + secretKey;
        if (sign.equals(CommonUtility.md5(stringToSign))) {
            TokenPo tokenPo;
            String orgUUID = organizationDao.getUUIDByAccessKey(accessKey);
            tokenPo = organizationDao.getToken(orgUUID, service, clientUUID);
            if(tokenPo == null || tokenPo.getTimeExpired() - CommonUtility.getCurrentTimestamp() < 300) {
                String token = CommonUtility.md5(UUID.randomUUID().toString() + stringToSign);
                Long timeCreated = CommonUtility.getCurrentTimestamp();
                Long timeExpired = timeCreated + 3600;
                tokenPo = new TokenPo();
                tokenPo.setToken(token);
                tokenPo.setOrgUUID(orgUUID);
                tokenPo.setService("report");
                tokenPo.setClientUUID(clientUUID);
                tokenPo.setTimeCreated(timeCreated);
                tokenPo.setTimeExpired(timeExpired);
                organizationDao.addToken(tokenPo);
            }
            return tokenPo;
        } else {
            throw new HttpException("sign_auth_error");
        }
    }

    @Override
    public TokenPo getToken(String token) {
        return organizationDao.getTokenByKey(token);
    }

    @Override
    public Boolean checkOrganizationPermission(String orgUUID, PermissionEnum permissionEnum) {
        byte[] permission = organizationDao.getPermissionByOrgUUID(orgUUID);
        if (permissionEnum == null) {
            return Boolean.FALSE;
        }
        int permissionPosition = permissionEnum.ordinal();
        if (((byte)(permission[permissionPosition/8] >> permissionPosition) & 1) == 1) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
