package com.datnguyen.rem.entity;

import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@MappedSuperclass
public abstract class AbstractEntity {
    @CreationTimestamp
    Date time_Create;
    @UpdateTimestamp
    Date time_Update;
}
