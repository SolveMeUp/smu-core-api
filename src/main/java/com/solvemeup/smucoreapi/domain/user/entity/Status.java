package com.solvemeup.smucoreapi.domain.user.entity;

/**
 * 사용자 계정 상태.
 *
 * <p>사용자의 서비스 접근 가능 여부 및
 * 서비스 내에서의 가시성을 결정한다.
 */
public enum Status {

    /**
     * 정상 사용자, 로그인 가능
     */
    ACTIVE,

    /**
     * 제재 상태, 로그인 불가
     */
    BLOCKED,

    /**
     * 탈퇴 상태, 서비스에서 숨김 처리
     */
    DELETED,

    /**
     * 개인정보 익명화 완료 상태
     */
    ANONYMIZED
}
