package com.yangyang.cloud.common;

import org.springframework.util.StringUtils;

import com.yangyang.cloud.common.exception.model.ErrorCodeEntity;

/**
 * Author: mengfanlong
 * E-mail: meng.fanlong@inspur.com
 * Date:   2018/9/27 13:51
 * -------------------------------
 * Desc:   通用返回值
 */
public class ResponseBean<T> {
	public static final String CODE_NAME = "code";
	public static final String MESSAGE_NAME = "message";
	public static final String RESULT_NAME = "result";
    /**
     * 返回码 0成功，其他失败
     */
    private String code;
    
    /**
     * 描述信息
     */
    private String message;
    
    /**
     * 返回结果
     */
    private T result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    private String requestId;
    public ResponseBean() {
		super();
	}

	public ResponseBean(String code, String message, T result) {
		super();
		this.code = code;
		this.message = message;
		this.result = result;
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
    
    public void setMessage(ErrorCodeEntity errorCodeEntity) {
        code = "";
        if (!StringUtils.isEmpty(errorCodeEntity.getProjectCode())) {
        	code += errorCodeEntity.getProjectCode();
        }
        code += ".";
        if (!StringUtils.isEmpty(errorCodeEntity.getModuleCode())) {
        	code += errorCodeEntity.getModuleCode();
        }
        if (!StringUtils.isEmpty(errorCodeEntity.getErrorCode())) {
        	code += errorCodeEntity.getErrorCode();
        }
        message = errorCodeEntity.getErrorMessage();
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
