package com.solvemeup.smucoreapi.domain.community.exception;

import com.solvemeup.smucoreapi.global.exception.BusinessException;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.COMMUNITY_POST_NOT_FOUND;

public class PostNotFoundException extends BusinessException {

    public PostNotFoundException() {
        super(COMMUNITY_POST_NOT_FOUND);
    }
}
