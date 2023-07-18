package com.kyozhou.jbooter.controller;

import com.kyozhou.jbooter.service.SourceService;
import com.kyozhou.jbooter.service.po.SourcePo;
import com.kyozhou.jbooter.system.constant.RegexConst;
import com.kyozhou.jbooter.config.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping("/source")
public class SourceController {

    @Autowired
    private SourceService sourceService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public SourcePo register(
            @Pattern(regexp = RegexConst.UUID36, message = "param_pattern_error") @RequestParam(value = "source_uuid") String sourceUUID,
            @RequestParam(value = "timezone", required = false, defaultValue = "8") Integer timezone,
            @RequestAttribute("_org_uuid") String orgUUID
    ) throws HttpException {
        return sourceService.register(orgUUID, sourceUUID, timezone);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<SourcePo> getSourceList(
            @RequestAttribute("_org_uuid") String orgUUID
    ) {
        return sourceService.getSourceList(orgUUID);
    }
}
