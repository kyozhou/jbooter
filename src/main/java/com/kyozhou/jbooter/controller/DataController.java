package com.kyozhou.jbooter.controller;

import com.kyozhou.jbooter.config.HttpException;
import com.kyozhou.jbooter.service.DataService;
import com.kyozhou.jbooter.service.po.StandardRawDataPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;

    @RequestMapping(value = "/standard-raw-data", method = RequestMethod.POST)
    public Boolean addRawData(
//            @PathVariable("source_uuid") String sourceUUID,
            @RequestAttribute("_org_uuid") String orgUUID,
            @RequestBody List<StandardRawDataPo> standardRawDataPoList
    ) throws HttpException {
        if (standardRawDataPoList == null) {
            throw new HttpException("data_invalid");
        } else if (standardRawDataPoList.size() == 0) {
            throw new HttpException("data_invalid");
        } else {
            dataService.addStandardRawDataMulti(standardRawDataPoList, orgUUID);
            return true;
        }
    }
}
