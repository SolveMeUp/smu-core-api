package com.solvemeup.smucoreapi.domain.user.entity;

/**
 * 사용자 권한 역할.
 *
 * <p>사용자가 수행할 수 있는 기능 범위를 정의한다.
 */
public enum Role {

    /**
     * 일반 사용자
     */
    USER,

    /**
     * 유료/구독 사용자
     */
    SUBSCRIBER,

    /**
     * 관리자
     */
    ADMIN
}
