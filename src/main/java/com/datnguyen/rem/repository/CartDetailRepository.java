package com.datnguyen.rem.repository;

import com.datnguyen.rem.dto.request.CartDetailRequest;
import com.datnguyen.rem.entity.CartDetail;
import com.datnguyen.rem.entity.embedded.CartDetail_ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, CartDetail_ID> {
    List<CartDetail> findByCartDetailIdUserId(String userId);
    CartDetail findByCartDetailIdUserIdAndCartDetailIdProductId(String userId,String ProductId);
    long deleteByCartDetailId_User_Id(String id);
}