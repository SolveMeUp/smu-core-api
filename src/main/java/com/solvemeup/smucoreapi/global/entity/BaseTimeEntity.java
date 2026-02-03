package com.solvemeup.smucoreapi.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 생성 시각과 수정 시각을 관리하는 베이스 엔티티.
 *
 * <p>엔티티가 생성될 때 createdAt,
 * 수정될 때마다 updatedAt이 자동으로 갱신된다.
 *
 * <p>수정 가능한 도메인 엔티티에 사용한다.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity extends CreatedTimeEntity {

    /**
     * 엔티티 최종 수정 시각
     *
     * <p>엔티티가 변경될 때마다 자동 갱신된다.
     */
    @LastModifiedDate
    @Column(nullable = false)
    protected LocalDateTime updatedAt;
}
