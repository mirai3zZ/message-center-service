package com.yangyang.cloud.common.exception.handler;

import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author sunguangtao 2018-10-13
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler extends BaseExceptionHandler {

    /**
     * 自定义错误（已知）
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseBean handleKnownException(BaseException ex) {
        return wrapper(ex);
    }

    /**
     * 请求参数错误
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({ServletRequestBindingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBean requestParameterExceptionHandler(ServletRequestBindingException ex) {
        return wrapper(ex);
    }

    /**
     * 内部错误（未知）
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseBean handleUnKnownException(Exception ex) {
        return wrapper(ex);
    }
}
