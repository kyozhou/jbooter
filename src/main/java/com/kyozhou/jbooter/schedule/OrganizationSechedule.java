package com.kyozhou.jbooter.schedule;

import com.kyozhou.jbooter.service.dao.OrganizationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrganizationSechedule {

    @Autowired
    private OrganizationDao organizationDao;

    @Scheduled(fixedRate = 3000)
    public void tokenChecker() {
        organizationDao.expireToken();
    }
}
