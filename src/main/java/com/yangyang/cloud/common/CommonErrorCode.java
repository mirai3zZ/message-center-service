package com.yangyang.cloud.common;

import com.yangyang.cloud.common.exception.model.ErrorCodeEntity;
import com.yangyang.cloud.common.exception.service.ErrorCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yuanye
 * 平台保留错误码
 */
@Component
public class CommonErrorCode {
    @Autowired
    private ErrorCodeService errorCodeService;
    /**
     * 系统根错误码
     */
    private static String baseErrorCode = "802";
    /**
     * 云平台保留错误码
     */
    public static String PARAM_VALUE_ERROR = baseErrorCode + ".999400";
    public static String PARAM_VALUE_ERROR_DESC = "参数取值错误";
    public static String PARAM_ERROR_LACK = baseErrorCode + ".998400";
    public static String PARAM_ERROR_LACK_DESC = "缺少参数错误，必传参数没填";
    public static String PARAM_ERROR_UNKNOWN = baseErrorCode + ".997400";
    public static String PARAM_ERROR_UNKNOWN_DESC = "未知参数错误，用户多传未定义的参数会导致错误";
    public static String PARAM_ERROR_TIMES_LIMIT = baseErrorCode + ".996400";
    public static String PARAM_ERROR_TIMES_LIMIT_DESC = "请求的次数超过了频率限制";
    public static String PARAM_ERROR_REGION_NOT_SUPPORT = baseErrorCode + ".995400";
    public static String PARAM_ERROR_REGION_NOT_SUPPORT_DESC = "接口不支持所传地域";
    public static String ERROR_AUTH = baseErrorCode + ".999401";
    public static String ERROR_AUTH_DESC = "鉴权错误";
    public static String ERROR_NOT_AUTH = baseErrorCode + ".999403";
    public static String ERROR_NOT_AUTH_DESC = "未授权操作";
    public static String NOT_FOUND = baseErrorCode + ".999404";
    public static String NOT_FOUND_DESC = "接口不存在";
    public static String INTERNAL_SERVER_ERROR = baseErrorCode + ".999500";
    public static String INTERNAL_SERVER_ERROR_DESC = "系统内部错误 ";

    /**
     * get message error bean
     *
     * @param errorCode
     * @return
     */
    public ErrorCodeEntity getErrorMessage(String moduleErrorCode, String errorCode) {
        ErrorCodeEntity errorCodeEntity = errorCodeService.getErrorCodeEntity(CommonErrorCode.baseErrorCode, moduleErrorCode, errorCode);
        errorCodeEntity.setErrorCode(CommonErrorCode.baseErrorCode + "." + moduleErrorCode + errorCode);
        return errorCodeEntity;
    }

}
