package com.yangyang.cloud.message.common;

import com.yangyang.cloud.common.CommonErrorCode;
import com.yangyang.cloud.common.exception.model.ErrorCodeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yuanye
 * @date 2018-12-11
 * Desc:message error code
 */
@Component
public class MessageErrorCode {
    @Autowired
    private CommonErrorCode commonErrorCode;

    public static final String messageErrorCode = "002";

    /**
     * message module business error code list
     * ...
     */
    public static final String MESSAGE_RECEIVER_ID_NOT_EMPTY = "002";
    public static final String MESSAGE_TEMPLATE_NOT_LEGAL = "003";
    public static final String MESSAGE_HAVA_MESSAGE_TYPE="004";
    public static final String MESSAGE_MAINTYPE_NAME_LIVE="005";
    public static final String MESSAGE_TYPE_NOT_EXIST = "006";
    public static final String GET_USERINFO_FAIL="008";
    public static final String NON_EXISTTEN_MESSAGE_TYPE="010";
    public static final String UPDATE_MESSAGE_TYPE_NAME_FIAL="011";
    public static final String MAIN_MESSAGE_TYPE_NAME_EXIST="012";
    public static final String MESSAGE_TYPE_NAME_EXIST="013";
    public static final String MESSAGE_SEND_FAILURE = "009";
    public static final String SEND_VERIFY_CODE_INTERVAL = "022";
    public static final String SEND_VERIFY_CODE_TIMES_ERROR = "023";
    public static final String VERIFY_CODE_INVALID = "024";
    public static final String VERIFY_CODE_ERROR = "025";
    public static final String VERIFY_CODE_TIMES_ERROR = "026";
    public static final String UPDATE_MESSAGE_SEND_SETTING_FAILURE = "027";
    public static final String  WHITE_MESSAGE_SEND_FAILURE = "035";
    public static final String MESSAGE_SYSTEM_STATUS_OFF = "034";

    public ErrorCodeEntity getMessageErrorMsg(String errorCode) {
        return commonErrorCode.getErrorMessage(messageErrorCode, errorCode);
    }
}
