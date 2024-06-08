package com.datnguyen.rem.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@MappedSuperclass
@Getter
public abstract class AbstractEntity {
    @CreationTimestamp
    Date timeCreate;
    @UpdateTimestamp
    Date timeUpdate;
}
