package com.kyozhou.jbooter.controller;

import com.kyozhou.jbooter.pojo.po.Token;
import com.kyozhou.jbooter.system.constant.RegexConst;
import com.kyozhou.jbooter.system.config.SysException;
import com.kyozhou.jbooter.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @RequestMapping(value = "/get-token", method = RequestMethod.GET)
    public Token getToken(
            @RequestParam(value = "access_key") String accessKey,
            @Pattern(regexp = RegexConst.UUID36, message = "param_pattern_error") @RequestParam(value = "client_uuid") String clientUUID,
            @RequestParam(value = "service") String service,
            @RequestParam(value = "timestamp") Long timestamp,
            @RequestParam(value = "sign") String sign
    ) throws SysException {
        return organizationService.generateToken(accessKey, clientUUID, service, timestamp, sign);
    }

    @RequestMapping("/token/{token}")
    public Token checkToken(
            @PathVariable(value = "token") String token
    ) {
        return organizationService.getToken(token);
    }
}
