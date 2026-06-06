package com.solvemeup.smucoreapi.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class ModifiedTimeEntity extends CreatedTimeEntity {

    @LastModifiedDate
    @Column(nullable = false)
    protected LocalDateTime updatedAt;
}
