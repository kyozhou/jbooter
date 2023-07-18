package com.kyozhou.jbooter.controller;

import com.kyozhou.jbooter.service.po.StandardRawDataPo;
import com.kyozhou.jbooter.config.HttpException;
import com.kyozhou.jbooter.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/debug")
public class DebugController {

    @Autowired
    private DataService dataService;

    @RequestMapping(value = "/raw-data", method = RequestMethod.GET)
    public List<StandardRawDataPo> getRawData(
            @RequestParam("source_uuid") String sourceUUID,
            @RequestParam("day") String day,
            @RequestAttribute("_org_uuid") String orgUUID
    ) throws HttpException {
//        int i=0;
//        int j = 100/i;
//        return null;
        return dataService.getStandardRawDataByDay(orgUUID, sourceUUID, day);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(
            @RequestAttribute("_org_uuid") String orgUUID
    ) throws HttpException {
//        int i=0;
//        int j = 100/i;
        System.out.println("testing.......................");
        return "";
    }
}
