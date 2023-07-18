package com.kyozhou.jbooter.controller;

import com.kyozhou.jbooter.service.po.TokenPo;
import com.kyozhou.jbooter.system.constant.RegexConst;
import com.kyozhou.jbooter.config.HttpException;
import com.kyozhou.jbooter.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @RequestMapping(value = "/get-token", method = RequestMethod.GET)
    public TokenPo getToken(
            @RequestParam(value = "access_key") String accessKey,
            @Pattern(regexp = RegexConst.UUID36, message = "param_pattern_error") @RequestParam(value = "client_uuid") String clientUUID,
            @RequestParam(value = "service") String service,
            @RequestParam(value = "timestamp") Long timestamp,
            @RequestParam(value = "sign") String sign
    ) throws HttpException {
        return organizationService.generateToken(accessKey, clientUUID, service, timestamp, sign);
    }

    @RequestMapping("/token/{token}")
    public TokenPo checkToken(
            @PathVariable(value = "token") String token
    ) {
        return organizationService.getToken(token);
    }
}
