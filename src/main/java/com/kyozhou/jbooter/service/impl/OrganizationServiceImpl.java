package com.kyozhou.jbooter.service.impl;

import com.kyozhou.jbooter.system.utils.CommonUtility;
import com.kyozhou.jbooter.system.config.SysException;
import com.kyozhou.jbooter.dao.OrganizationDao;
import com.kyozhou.jbooter.pojo.po.PermissionEnum;
import com.kyozhou.jbooter.pojo.po.Token;
import com.kyozhou.jbooter.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationDao organizationDao;

    @Override
    public Token generateToken(String accessKey, String clientUUID, String service, Long timestamp, String sign) throws SysException {
        String secretKey = organizationDao.getSecretKeyByAccessKey(accessKey);
        String stringToSign = accessKey + clientUUID + service + timestamp.toString() + secretKey;
        if (sign.equals(CommonUtility.md5(stringToSign))) {
            Token tokenPo;
            String orgUUID = organizationDao.getUUIDByAccessKey(accessKey);
            tokenPo = organizationDao.getToken(orgUUID, service, clientUUID);
            if(tokenPo == null || tokenPo.getTimeExpired() - CommonUtility.getCurrentTimestamp() < 300) {
                String token = CommonUtility.md5(UUID.randomUUID().toString() + stringToSign);
                Long timeCreated = CommonUtility.getCurrentTimestamp();
                Long timeExpired = timeCreated + 3600;
                tokenPo = new Token();
                tokenPo.setToken(token);
                tokenPo.setOrgUuid(orgUUID);
                tokenPo.setService("report");
                tokenPo.setClientUuid(clientUUID);
                tokenPo.setTimeCreated(timeCreated);
                tokenPo.setTimeExpired(timeExpired);
                organizationDao.addToken(tokenPo);
            }
            return tokenPo;
        } else {
            throw new SysException("sign_auth_error");
        }
    }

    @Override
    public Token getToken(String token) {
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
