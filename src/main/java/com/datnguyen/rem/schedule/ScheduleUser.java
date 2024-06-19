package com.datnguyen.rem.schedule;

import com.datnguyen.rem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduleUser {
    @Autowired
    private UserRepository repository;
    @Scheduled(timeUnit = TimeUnit.DAYS,fixedRate = 1)
    @Transactional
    public void scheduleDeleteUserNotActive(){
        Date date= Date.from(Instant.now().minusSeconds(360));
        repository.deleteByTimeCreateLessThanAndIsActiveFalse(date);
    }
}
