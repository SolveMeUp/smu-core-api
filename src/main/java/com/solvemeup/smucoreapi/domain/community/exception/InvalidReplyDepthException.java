package com.solvemeup.smucoreapi.domain.community.exception;

public class InvalidReplyDepthException extends RuntimeException {

    public InvalidReplyDepthException() {
        super("대댓글에는 답글을 작성할 수 없습니다.");
    }
}
