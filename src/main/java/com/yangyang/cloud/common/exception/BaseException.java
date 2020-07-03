package com.yangyang.cloud.common.exception;

import com.yangyang.cloud.common.ResponseBean;
import lombok.Data;

/**
 * All self-defined extended exception must inherit BaseException class.
 *
 * @author sunguangtao 2018-10-13
 */
@Data
public class BaseException extends RuntimeException {
    // error code
    protected String code;
    protected String message;
    protected String requestId;

    public BaseException() {
    }

    public BaseException(String code, String message,String requestId) {
        this.code = code;
        this.message = message;
        this.requestId = requestId;
    }

    public ResponseBean generateResponseBean() {
        ResponseBean responseBean = new ResponseBean();
        //TODO message get from db
        responseBean.setMessage(this.message);
        responseBean.setCode(this.code);
        return responseBean;
    }

    public BaseException(String message) {
        super(message);
    }public BaseException(String message,Throwable e) {
        super(message,e);
    }
}
