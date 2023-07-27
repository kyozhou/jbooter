package com.kyozhou.jbooter.service;

import com.kyozhou.jbooter.pojo.po.PermissionEnum;
import com.kyozhou.jbooter.pojo.po.Token;
import com.kyozhou.jbooter.system.config.SysException;

public interface OrganizationService {

    Token getToken(String token);
    Boolean checkOrganizationPermission(String orgUUID, PermissionEnum permissionEnum);
    Token generateToken(String accessKey, String clientUUID, String service, Long timestamp, String sign) throws SysException;

}
