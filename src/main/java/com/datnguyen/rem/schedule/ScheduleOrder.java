package com.datnguyen.rem.schedule;

import com.datnguyen.rem.enums.PaymentType;
import com.datnguyen.rem.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduleOrder {
    @Autowired
    private OrderRepository repository;
    @Scheduled(timeUnit = TimeUnit.DAYS,fixedRate = 1)
    @Transactional
    public void deleteOrderOnlinePaymentIsNotPaid(){
        Date date= Date.from(Instant.now().minus(1, ChronoUnit.DAYS));
        repository.deleteByTimeCreateGreaterThanAndPaymentTypeAndIsPaidFalse(date,PaymentType.VNPAY);
    }
}
