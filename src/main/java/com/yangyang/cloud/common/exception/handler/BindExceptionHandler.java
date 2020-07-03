package com.yangyang.cloud.common.exception.handler;

import com.yangyang.cloud.common.RequestIdUtil;
import com.yangyang.cloud.common.ResponseBean;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static com.yangyang.cloud.common.CommonErrorCode.PARAM_VALUE_ERROR;
import static com.yangyang.cloud.common.CommonErrorCode.PARAM_VALUE_ERROR_DESC;

/**
 * handler for exception thrown by restful controller when binding request parameter
 *
 * @author sunguangtao 2018-10-12
 */
@RestControllerAdvice
public class BindExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBean<List<ParameterValidationMessage>> handleBindException(MethodArgumentNotValidException ex) {
        ResponseBean<List<ParameterValidationMessage>> errorResult = new ResponseBean<>();
        errorResult.setCode(PARAM_VALUE_ERROR);
        errorResult.setMessage(PARAM_VALUE_ERROR_DESC);
        errorResult.setRequestId(RequestIdUtil.requestIdThreadLocal.get());
        List<ParameterValidationMessage> validationMessages = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationMessages.add(new ParameterValidationMessage(fieldError));
        }
        errorResult.setResult(validationMessages);
        return errorResult;
    }
    
    /**
     * 处理 get 请求的参数在验证框架下的异常
     * @param bindException BindException
     * @return ResponseBean<List<ParameterValidationMessage>>
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBean<List<ParameterValidationMessage>> handleTheBindException(BindException bindException) {
        ResponseBean<List<ParameterValidationMessage>> errorResult = new ResponseBean<>();
        errorResult.setCode(PARAM_VALUE_ERROR);
        errorResult.setMessage(PARAM_VALUE_ERROR_DESC);
        errorResult.setRequestId(RequestIdUtil.requestIdThreadLocal.get());
        List<ParameterValidationMessage> validationMessages = new ArrayList<>();
        for (FieldError fieldError : bindException.getBindingResult().getFieldErrors()) {
            validationMessages.add(new ParameterValidationMessage(fieldError));
        }
        errorResult.setResult(validationMessages);
        return errorResult;
    }
}

@Data
class ParameterValidationMessage {
    private String description;
    private String parameter;
    private Object value;
    private String code;

    ParameterValidationMessage(FieldError fieldError) {
        this.description = fieldError.getDefaultMessage();
        this.parameter = fieldError.getField();
        this.code = fieldError.getCode();
        this.value = fieldError.getRejectedValue();
    }
}
