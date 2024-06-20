package com.datnguyen.rem.repository;

import com.datnguyen.rem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    void deleteByTimeCreateLessThanAndIsActiveFalse(Date timeCreate);

    Optional<User> findByEmail(String email);

}
