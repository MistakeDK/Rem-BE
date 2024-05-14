package com.datnguyen.rem.repository;

import com.datnguyen.rem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByusername(String username);
    Optional<User> findByusername(String username);
}
