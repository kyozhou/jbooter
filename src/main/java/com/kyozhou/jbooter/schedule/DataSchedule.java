package com.kyozhou.jbooter.schedule;

import com.kyozhou.jbooter.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataSchedule {

    @Autowired
    private DataService dataService;

    @Scheduled(cron = "0 45 11 ? * 1")
    public void clearData() {
//        dataService.clearStandardRawData();
    }
}
