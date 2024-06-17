package com.datnguyen.rem.entity;

import com.datnguyen.rem.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(unique = true)
    String username;
    String password;
    @Column(unique = true)
    String email;
    String phone;
    LocalDate dob;
    Boolean isActive;
    Boolean isBan;
    @Enumerated(EnumType.STRING)
    Role role=Role.USER;
    @Transient
    String verificationCode;
}
