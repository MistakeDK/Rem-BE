package com.datnguyen.rem.repository;

import com.datnguyen.rem.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,String>, JpaSpecificationExecutor<Promotion> {
}
