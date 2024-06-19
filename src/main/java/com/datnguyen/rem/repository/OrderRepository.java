package com.datnguyen.rem.repository;

import com.datnguyen.rem.entity.Order;
import com.datnguyen.rem.enums.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OrderRepository extends JpaRepository<Order,String> {
    Page<Order> findByUser_Id(String id, Pageable pageable);
    long deleteByTimeCreateGreaterThanAndPaymentTypeAndIsPaidFalse(Date timeCreate, PaymentType paymentType);
}
