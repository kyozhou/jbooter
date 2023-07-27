package com.kyozhou.jbooter.schedule;

import com.kyozhou.jbooter.dao.OrganizationDao;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationSchedule {

    private final OrganizationDao organizationDao;

    @Scheduled(fixedRate = 3000)
    public void tokenChecker() {
        organizationDao.expireToken();
    }
}
