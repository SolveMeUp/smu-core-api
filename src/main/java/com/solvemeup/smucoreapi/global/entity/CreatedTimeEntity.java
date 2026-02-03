package com.solvemeup.smucoreapi.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 생성 시각(createdAt)만을 관리하는 베이스 엔티티.
 *
 * <p>엔티티가 최초로 저장될 때의 시각을 자동으로 기록한다.
 * 수정이나 삭제 개념이 없는 불변(immutable) 엔티티에 사용한다.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CreatedTimeEntity {

    /**
     * 엔티티 생성 시각
     *
     * <p>최초 생성 시 자동 설정되며,
     * 이후 변경되지 않는다.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;
}
