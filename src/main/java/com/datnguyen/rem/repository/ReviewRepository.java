package com.datnguyen.rem.repository;

import com.datnguyen.rem.entity.Review;
import com.datnguyen.rem.entity.embedded.Review_ID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Review_ID> {
    Page<Review> findById_Product_Id(String id, Pageable pageable);

    long countById_Product_Id(String id);

    @Query("SELECT AVG(r.rateStar) FROM Review r WHERE r.id.product.id = ?1")
    Double calculateAVG(String id);
}
