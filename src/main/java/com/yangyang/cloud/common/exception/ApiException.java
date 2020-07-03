package com.yangyang.cloud.common.exception;

import com.yangyang.cloud.common.RequestIdUtil;

/**
 * Exception thrown when invoke api of other module
 *
 * @author sunguangtao 2018-10-13
 */
public class ApiException extends BaseException {
    private static String requestId = RequestIdUtil.requestIdThreadLocal.get();

    public ApiException(String code, String message) {

        super(code, message, requestId);
    }
}
