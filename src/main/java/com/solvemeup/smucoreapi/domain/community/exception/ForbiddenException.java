package com.solvemeup.smucoreapi.domain.community.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public static ForbiddenException postModify() {
        return new ForbiddenException("게시글을 수정할 권한이 없습니다.");
    }

    public static ForbiddenException postDelete() {
        return new ForbiddenException("게시글을 삭제할 권한이 없습니다.");
    }

    public static ForbiddenException commentDelete() {
        return new ForbiddenException("댓글을 삭제할 권한이 없습니다.");
    }
}
