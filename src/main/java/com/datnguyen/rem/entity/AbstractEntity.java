package com.datnguyen.rem.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@MappedSuperclass
@Setter
public abstract class AbstractEntity {
    @CreationTimestamp
    @Getter
    Date timeCreate;
    @UpdateTimestamp
    Date timeUpdate;
}
