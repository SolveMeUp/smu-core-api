package com.solvemeup.smucoreapi.domain.community.exception;

import com.solvemeup.smucoreapi.global.exception.BusinessException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.COMMUNITY_COMMENT_NOT_FOUND;

public class CommentNotFoundException extends BusinessException {

    public CommentNotFoundException() {
        super(COMMUNITY_COMMENT_NOT_FOUND);
    }
}
