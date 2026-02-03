package com.solvemeup.smucoreapi.domain.auth.oauth2.principal;

/**
 * OAuth2 로그인 과정에서 발생한 사용자 상태 이벤트.
 *
 * <ul>
 *   <li>{@link #NONE} : 기존 사용자 로그인</li>
 *   <li>{@link #SIGNED_UP} : 신규 회원 가입</li>
 *   <li>{@link #RESTORED_FROM_DELETED} : 탈퇴 계정 복구</li>
 *   <li>{@link #RESTORED_FROM_ANONYMIZED} : 익명화 계정 복구</li>
 * </ul>
 *
 * <p>프론트엔드에서 로그인 결과에 따른 UI 분기를 위해 사용된다.
 */
public enum LoginEvent {
    NONE,
    SIGNED_UP,
    RESTORED_FROM_DELETED,
    RESTORED_FROM_ANONYMIZED
}
