package com.yangyang.cloud.message.common;

/**
 * description: 公共常量处理
 *
 * @author renhaixiang
 * @version 1.0
 * @date 2018/9/30 9:59
 **/
public class MessageConstants {
    public static final String RECEIVER_NO_EXIST = "无消息接收人。";
    public static final String MESSAGE_UNREAD_QUERY_SUCCESS = "未读消息查询成功";
    public static final String RECEIVER_LIST_SUCCESS = "消息接收人展示成功。";
    public static final String MESSAGE_GET_ALL = "all";
    public static final String MESSAGE_GET_OTHER = "other";
    public static final String MESSAGE_RECEIVER_PARAM_NULL = "消息接受人参数不能为空。";
    public static final String MESSAGE_RECEIVER_NAME_NULL = "消息接收人姓名不能为空。";
    public static final String MESSAGE_RECEIVER_EMAIL_NULL = "消息接收人邮箱不能为空";
    public static final String EMAIL_FORMAT_NOT_CORRECT = "消息接收人邮箱格式不正确。";
    public static final String MESSAGE_RECEIVER_MOBILE_NULL = "消息接受人手机号不能为空。";
    public static final String MOBILE_FORMAT_NOT_CORRECT = "消息接收人手机号格式不正确";
    public static final String MESSAGE_RECEIVER_ADD_SUCCESS = "消息接收人添加成功。";
    public static final String MESSAGE_RECEIVER_UPDATE_SUCCESS = "消息接收人修改成功";
    public static final String MESSAGE_RECEIVER_REMOVE_SUCCESS = "消息接收人删除成功";
    public static final String USER_MESSAGE_EAMIL_SUBJECT = "消息接收验证通知";
    public static final String USER_MESSAGE_CONTENT = "尊敬的用户您好：您已被[%s]设置为该账户的消息接收人，若同意接受该账户下的消息通知，请点击链接确认[%s]";
    public static final String USER_MESSAGE_ACTIVITE_PASS = "AES/Inspur_PASS_";
    public static final int MESSAGE_MAX_LIST = 50;
    public static final int MESSAGE_DEFAULT_SIZE = 4;
    public static final int MESSAGE_NUM_ZERO = 0;
    public static final String PROTOCOL = "http://";
    public static final String MODULE_FORWARD = "picp-forward";
    public static final String MODULE_USERCENTER = "user-service";
    public static final String DELETE_MESSAGE_SUCCESS = "消息删除成功";
    public static final String DELETE_MESSAGE_SUCCESS_CODE = "0";
    public static final String DELETE_MESSAGE_FAIL = "消息删除失败";
    public static final String DELETE_MESSAGE_FAIL_CODE = "1";
    public static final String MARK_AS_READ_SUCCESS = "标记为已读成功";
    public static final String MARK_AS_READ_SUCCESS_CODE = "0";
    public static final String MARK_AS_READ_FAIL_CODE = "1";
    public static final String MARK_AS_READ_FAIL = "标记已读失败";

    public static final long ONE_DAY_TIME_SPAN = 60 * 60 * 1000 * 24;

    public static final String BATCH_RECEIVER_ADD_SUCCESS = "批量添加接收人成功";
    public static final String BATCH_RECEIVER_ADD_SUCCESS_CODE = "0";
    public static final String BATCH_RECEIVER_ADD_FAIL_CODE = "1";
    public static final String BATCH_RECEIVER_ADD_FAIL = "批量添加接收人失败";
    public static final String BATCH_RECEIVER_DELETE_FAIL = "批量删除接收人失败";
    public static final String BATCH_RECEIVER_DELETE_FAIL_CODE = "1";
    public static final String BATCH_RECEIVER_DELETE_SUCCESS_CODE = "0";
    public static final String BATCH_RECEIVER_DELETE_SUCCESS = "批量删除接收人成功";
    public static final String DELETE_MESSAGE_TYPE_SUCCESS = "删除消息类型成功";
    public static final String DELETE_MESSAGE_TYPE_SUCCESS_CODE = "0";
    public static final String DELETE_MESSAGE_TYPE_FAIL_CODE = "0";
    public static final String DELETE_MESSAGE_TYPE_FAIL = "删除消息类型失败";
    public static final String DELETE_MESSAGE_CLASSIFY_FAIL = "删除消息分类失败";
    public static final String DELETE_MESSAGE_CLASSIFY_FAIL_CODE = "1";
    public static final String DELETE_MESSAGE_CLASSIFY_SUCCESS_CODE = "0";
    public static final String DELETE_MESSAGE_CLASSIFY_SUCCESS = "删除消息分类成功";
    public static final String COMMON_CONFIG_GROUP = "operation-service";
    public static final String COMMON_CONFIG_RECEIVER_INVITATION_URL = "RECEIVER_INVITATION_URL";
    public static final String COMMON_CONFIG_RECEIVER_INVITATION_TEMPLATE_ID = "RECEIVER_INVITATION_TEMPLATE_ID";
    public static final String COMMON_CONFIG_RECEIVER_DELETE_TEMPLATE_ID = "message_delete_receiver";
    public static final String GAIN_RECEIVER_CONTACT_SUCCESS = "获取消息接收人联系方式成功";
    public static final String GAIN_RECEIVER_CONTACT_FAILURE = "获取消息接收人联系方式失败";
    public static final String NO_EXISTENCE_FOR_RECEIVER = "消息接收人不存在";


    public static final String RECEIVER_SELF = "账号联系人";
    public static final String RECEIVER_RELATED_MESSAGE_TYPE = "该用户为【%s】%s的唯一接收人，请先移除该联系人再删除";

    public static final String STATUS_YES = "Y";
    public static final String STATUS_NO = "N";
    public static final String UNKOWN = "unkown";
    public static final String SUCCESS_SEND_MESSAGE = "SUCCESS";
    public static final String FAILURE_SEND_MESSAGE = "FAILURE";
    public static final String READY_SEND_MESSAGE ="READY";
    public static final String MESSAGE_HANDLE_SUCCESS = "0";
    public static final String MAIL_SEND_SUCCESS = "站内信发送成功";
    public static final String MAIL_SEND_FAILURE = "站内信发送失败";
    public static final String EMAIL_SEND_SUCCESS = "邮件发送成功";
    public static final String EMAIL_SEND_FAILURE = "邮件发送失败";
    public static final String EMAIL_SEND_PROCESSING ="邮件待发送";
    public static final String SMS_SEND_SUCCESS = "短信发送成功";
    public static final String SMS_SEND_FAILURE = "短信发送失败";
    public static final String MESSAGE_SEND_SUCCESS = "消息发送成功";
    public static final String MESSAGE_SEND_WHITE="白名单";
    public static final String OPERATION_YES = "Y";
    public static final String OPERATION_NO = "N";
    public static final String SEND_TARGET_ACCOUNT = "account";
    public static final String SEND_TARGET_CONTACT = "contact";
    public static final int RECEIVER_MAX_NUM = 100;
    public static final String RECEIVER_OVERFLOW_INFO = "每个账户最多设置100个消息接收人，请合理管理消息接收人";
    public static final String STRING_ZERO = "0";
    public static final String STRING_ONE = "1";
    public static final String MERGE_SEND_YES= "Y";
    public static final String MERGE_SEND_PROCESSING= "N";
    public static final String MERGE_SEND_FAILURE = "F";
    public static final String JOB_EXECUTOR_HANDLER="emailSendHandler";
    public static final String EXECUTOR_BLOCK_STRATEGY="SERIAL_EXECUTION";
    /** 验证码redis 保存常量 */
    public static final String REDIS_KEY = "sms-identifyingCode:";
    public static final String MOBILE_CODE_HAS_ACTION = "mobileCodeHasAction";
    public static final String MOBILE_CODE_NO_ACTION = "mobileCodeNoAction";
    public static final String VALID_PERIOD_ERROR_CODE = "1";
    public static final int MAX_VALID_PERIOD = 120;
    public static final String SEND_BY_SMS = "sms";
    public static final int MAX_VERIFY_CODE_TIMES = 10;
    public static final String CODE_PERMIT_ERROR_TIMES = "codePermitErrorTimes";
    public static final String NONE_VALID_EXISTENCE_MESSAGE_TYPE = "不存在有效的消息类型";
    public static final String MESSAGE_TYPE_OTHER = "其他";
    public static final String MESSAGE_TYPE_ALL = "全部";
    public static final String MESSAGE_USING_OBJECT_TYPE_INNER = "inner";
    public static final String MESSAGE_SETTING_ALL = "all";
    public static final String MESSAGE_SETTING_SOME = "some";
    public static final String MESSAGE_SETTING_NONE = "none";
    public static final String CURRENT_SUPPLIER_COMMON_CONFIG_GROUPNAME = "operation-service-message";
    public static final String CURRENT_SWITCH_COMMON_CONFIG_NAME = "operation_message_switch";
    public static final String CURRENT_MESSAGE_SYSTEM_STATUS = "OFF";
    /**
     * The minimum priority that a thread can have.
     */
    public final static int MIN_PRIORITY = 0;

    /**
     * The default priority that is assigned to a thread.
     */
    public final static int NORM_PRIORITY = 5;

    /**
     * The maximum priority that a thread can have.
     */
    public final static int MAX_PRIORITY = 10;

    /**
     * cron expression
     */
    public static final String   SCHEDULE_TIME ="0 0 0/%s * * ?";
}
