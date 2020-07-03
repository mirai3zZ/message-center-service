package com.yangyang.cloud.common.exception.handler;

import com.yangyang.cloud.common.RequestIdUtil;
import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.yangyang.cloud.common.CommonErrorCode.*;

/**
 * @author sunguangtao 2018-10-13
 */
@Slf4j
public class BaseExceptionHandler {
    protected ResponseBean exceptionEntity = new ResponseBean();

    protected ResponseBean wrapper(BaseException ex) {
        exceptionEntity.setCode(ex.getCode());
        exceptionEntity.setMessage(ex.getMessage());
        exceptionEntity.setRequestId(ex.getRequestId());
        return exceptionEntity;
    }

    protected ResponseBean wrapper(Exception ex) {
        if (ex instanceof NoHandlerFoundException) {
            exceptionEntity.setMessage(NOT_FOUND_DESC);
            exceptionEntity.setCode(NOT_FOUND);
        } else if (ex instanceof ServletRequestBindingException) {
            exceptionEntity.setMessage(PARAM_ERROR_LACK_DESC);
            exceptionEntity.setCode(PARAM_ERROR_LACK);
        } else if (ex instanceof BaseException){
            return ((BaseException) ex).generateResponseBean();
        } else {
            exceptionEntity.setMessage(INTERNAL_SERVER_ERROR_DESC);
            exceptionEntity.setCode(INTERNAL_SERVER_ERROR);
        }
        exceptionEntity.setRequestId(RequestIdUtil.requestIdThreadLocal.get());
        //打印错误堆栈信息，考虑性能时可以去掉
        log.error("ERROR", "ERROR DETAIL:", ex);
        return exceptionEntity;
    }
}
