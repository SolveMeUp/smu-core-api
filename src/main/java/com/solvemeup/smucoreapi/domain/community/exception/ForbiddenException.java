package com.solvemeup.smucoreapi.domain.community.exception;

import com.solvemeup.smucoreapi.global.exception.BusinessException;
import com.solvemeup.smucoreapi.global.exception.ErrorCode;

import static com.solvemeup.smucoreapi.global.exception.ErrorCode.COMMUNITY_COMMENT_FORBIDDEN;
import static com.solvemeup.smucoreapi.global.exception.ErrorCode.COMMUNITY_POST_FORBIDDEN;

public class ForbiddenException extends BusinessException {

    private ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static ForbiddenException postModify() {
        return new ForbiddenException(COMMUNITY_POST_FORBIDDEN);
    }

    public static ForbiddenException postDelete() {
        return new ForbiddenException(COMMUNITY_POST_FORBIDDEN);
    }

    public static ForbiddenException commentDelete() {
        return new ForbiddenException(COMMUNITY_COMMENT_FORBIDDEN);
    }
}
