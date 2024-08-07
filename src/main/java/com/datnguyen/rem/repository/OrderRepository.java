package com.datnguyen.rem.repository;

import com.datnguyen.rem.dto.response.OrderStatResponse;
import com.datnguyen.rem.entity.Order;
import com.datnguyen.rem.enums.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,String>, JpaSpecificationExecutor<Order> {
    Page<Order> findByUser_Id(String id, Pageable pageable);
    long deleteByTimeCreateGreaterThanAndPaymentTypeAndIsPaidFalse(Date timeCreate, PaymentType paymentType);
    @Query("SELECT new com.datnguyen.rem.dto.response.OrderStatResponse(o.status, COUNT(*)) " +
            "FROM Order o " +
            "GROUP BY o.status")
    List<OrderStatResponse> getStatOrder();
}
