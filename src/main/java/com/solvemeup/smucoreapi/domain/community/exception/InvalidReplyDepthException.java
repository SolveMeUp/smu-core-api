package com.solvemeup.smucoreapi.domain.community.exception;

import com.solvemeup.smucoreapi.global.exception.BusinessException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.COMMUNITY_INVALID_REPLY_DEPTH;

public class InvalidReplyDepthException extends BusinessException {

    public InvalidReplyDepthException() {
        super(COMMUNITY_INVALID_REPLY_DEPTH);
    }
}
