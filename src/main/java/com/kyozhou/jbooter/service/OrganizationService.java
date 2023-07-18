package com.kyozhou.jbooter.service;

import com.kyozhou.jbooter.pojo.po.PermissionEnum;
import com.kyozhou.jbooter.pojo.po.TokenPo;
import com.kyozhou.jbooter.system.config.HttpException;

public interface OrganizationService {

    TokenPo getToken(String token);
    Boolean checkOrganizationPermission(String orgUUID, PermissionEnum permissionEnum);
    TokenPo generateToken(String accessKey, String clientUUID, String service, Long timestamp, String sign) throws HttpException;

}
