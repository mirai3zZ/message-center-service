package com.yangyang.cloud.common.exception;

import com.yangyang.cloud.common.RequestIdUtil;

/**
 * @author sunguangtao 2018-10-13
 */
public class BusinessException extends BaseException {
    private static String requestId = RequestIdUtil.requestIdThreadLocal.get();

    public BusinessException(String code, String message) {
        super(code, message, requestId);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable e) {
        super(message, e);
    }
}
