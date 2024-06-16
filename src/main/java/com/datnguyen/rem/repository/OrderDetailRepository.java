package com.datnguyen.rem.repository;

import com.datnguyen.rem.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    Optional<OrderDetail> findByProduct_IdAndOrder_Id(String idProduct, String idOrder);
}
