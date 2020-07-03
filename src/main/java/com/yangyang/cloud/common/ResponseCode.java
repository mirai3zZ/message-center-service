package com.yangyang.cloud.common;

/**
 * @author: mengfanlong
 * E-mail:  meng.fanlong@inspur.com
 * Date:    2018/10/1 8:58
 * -------------------------------
 * Desc:    返回码和返回message
 */
public class ResponseCode {

    public static final String RESPONSE_CODE_SUCCESS = "0";
    public static final String RESPONSE_CODE_FAILURE = "1";
    public static final String RESPONSE_SUCCESS_MESSAGE = "处理成功";
    public static final String NOTICE_ADD_ERROR_CODE = "-1";
    public static final String NOTICE_ADD_EMPTY_ERROR_MESSAGE = "公告标题或者内容不能为空";
    public static final String METHOD_PARAMS_ERROR = "接收人参数不能同时为空";
    public static final String METHOD_PARAMS_ERROR_CODE = "400";
    public static final String REQUEST_PARAM_ERROR = "传递参数错误！";
}
