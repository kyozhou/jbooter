package com.kyozhou.jbooter.schedule;

import com.kyozhou.jbooter.config.HttpException;
import com.kyozhou.jbooter.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReportSechedule {

    @Autowired
    ReportService reportService;

    @Scheduled(cron = "0 0 * * * ?")
    public void reportBuilder() {
        try {
            reportService.buildReport();
        } catch (HttpException e) {
            e.printStackTrace();
        }
    }

}