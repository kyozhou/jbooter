package com.kyozhou.jbooter.service;

import com.kyozhou.jbooter.service.po.PermissionEnum;
import com.kyozhou.jbooter.service.po.TokenPo;
import com.kyozhou.jbooter.config.HttpException;

public interface OrganizationService {

    TokenPo getToken(String token);
    Boolean checkOrganizationPermission(String orgUUID, PermissionEnum permissionEnum);
    TokenPo generateToken(String accessKey, String clientUUID, String service, Long timestamp, String sign) throws HttpException;

}
