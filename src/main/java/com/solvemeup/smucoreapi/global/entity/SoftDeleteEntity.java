package com.solvemeup.smucoreapi.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Soft Delete를 지원하는 베이스 엔티티.
 *
 * <p>엔티티를 물리적으로 삭제하지 않고,
 * 삭제 시각(deletedAt)을 기록하는 방식으로 삭제를 표현한다.
 *
 * <p>deletedAt은 "삭제 이력"을 나타내는 필드이며,
 * 실제 활성/비활성 여부 판단은
 * 각 도메인의 상태(status) 정책에 맡긴다.
 */
@Getter
@MappedSuperclass
public abstract class SoftDeleteEntity extends BaseTimeEntity {

    /**
     * 삭제 시각 (soft delete)
     *
     * <p>null이면 삭제되지 않은 상태,
     * 값이 있으면 soft delete 상태를 의미한다.
     */
    @Column
    protected LocalDateTime deletedAt;

    /**
     * soft delete 상태를 복구한다.
     *
     * <p>deletedAt을 null로 초기화한다.
     */
    protected void restore() {
        this.deletedAt = null;
    }

    /**
     * 엔티티가 soft delete 상태인지 여부를 반환한다.
     *
     * @return 삭제 상태이면 true, 그렇지 않으면 false
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }
}
