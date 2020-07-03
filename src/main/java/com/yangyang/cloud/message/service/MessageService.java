package com.yangyang.cloud.message.service;

import com.inspur.bss.commonsdk.utils.IdWorker;
import com.yangyang.cloud.common.*;
import com.yangyang.cloud.common.bean.CommonConfigBean;
import com.yangyang.cloud.common.bean.User;
import com.yangyang.cloud.common.enums.MessageTemplateTypeEnum;
import com.yangyang.cloud.common.exception.ApiException;
import com.yangyang.cloud.common.exception.BusinessException;
import com.yangyang.cloud.common.exception.model.ErrorCodeEntity;
import com.yangyang.cloud.common.redis.RedisUtil;
import com.yangyang.cloud.fileoperation.bean.FileBean;
import com.yangyang.cloud.fileoperation.bean.OperationAttachmentInfo;
import com.yangyang.cloud.fileoperation.service.OperationAttachmentInfoService;
import com.yangyang.cloud.keycloak.SecurityContextUtil;
import com.yangyang.cloud.message.bean.OperationMessageEntity;
import com.yangyang.cloud.message.common.MessageConstants;
import com.yangyang.cloud.message.common.MessageErrorCode;
import com.yangyang.cloud.message.dto.MessageErrorDTO;
import com.yangyang.cloud.message.dto.SmsVerifyCodeDTO;
import com.yangyang.cloud.message.mapper.OperationMessageMapper;
import com.yangyang.cloud.message.mapper.OperationMessageTypeMapper;
import com.yangyang.cloud.message.vo.*;
import com.yangyang.cloud.messagemanage.bean.MessageBean;
import com.yangyang.cloud.messagemanage.bean.MessageTemplateBean;
import com.yangyang.cloud.messagemanage.bean.MessageTypeBean;
import com.yangyang.cloud.messagemanage.bean.OperationMessageBean;
import com.yangyang.cloud.messagemanage.mapper.MessageManageMapper;
import com.yangyang.cloud.messagemanage.service.MessageManageService;
import com.yangyang.cloud.messagemanage.vo.MessageSendStatusVo;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.util.ArrayListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.yangyang.cloud.message.common.MessageConstants.*;
import static com.yangyang.cloud.message.common.MessageErrorCode.MESSAGE_RECEIVER_ID_NOT_EMPTY;

/**
 * @author daiyan
 */
@Service
@Slf4j
public class MessageService {
    @Autowired
    private OperationMessageTypeMapper operationMessageTypeMapper;
    @Autowired
    private OperationMessageMapper operationMessageMapper;
    @Autowired
    private MessageManageService messageManageService;
    @Autowired
    private MessageErrorCode messageErrorCode;
    @Autowired
    private ApiManageUtil apiManageUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private MessageManageMapper messageManageMapper;
    @Autowired
    private OperationAttachmentInfoService operationAttachmentInfoService;
    /**
     * 数据未进行逻辑删除
     */
    private static final String DELETE_FLAG_N = "N";

    /**
     * 未读状态
     */
    private static final String READ_STATUS_N = "N";


    /**
     * 查询消息分类
     *
     * @return ResponseBean<List                                                                                                                                                                                                                                                               <                                                                                                                                                                                                                                                               MessageTypeListVO>>
     */
    public ResponseBean<List<MessageTypeListVO>> typeList() {
        return new ResponseBean<List<MessageTypeListVO>>(
                ResponseCode.RESPONSE_CODE_SUCCESS,
                ResponseCode.RESPONSE_SUCCESS_MESSAGE,
                operationMessageTypeMapper.getIdAndNames());
    }

    /**
     * 获取未读消息不同分类数量
     *
     * @return ResponseBean<List                                                                                                                                                                                                                                                               <                                                                                                                                                                                                                                                               MessageUnreadCountVO>>
     */
    public ResponseBean<List<MessageUnreadCountVO>> unreadCount() {
        ResponseBean<List<MessageUnreadCountVO>> responseBean = new ResponseBean<List<MessageUnreadCountVO>>();
        List<MessageUnreadCountVO> messageUnreadCountVOList = new ArrayList<MessageUnreadCountVO>();
        // 获取当前用户
        User loginUser = SecurityContextUtil.getLoginUser();

        // 根据当前用户获取所有未读消息
        List<OperationMessageEntity> unreadMessageList = operationMessageMapper.getList(
                DELETE_FLAG_N, READ_STATUS_N, loginUser.getId(), MessageTemplateTypeEnum.MAIL.getType());
        if (!CollectionUtils.isEmpty(unreadMessageList)) {

            // 将未读消息按 messageMainType 升序排列
            Collections.sort(unreadMessageList, new Comparator<OperationMessageEntity>() {
                @Override
                public int compare(OperationMessageEntity o1, OperationMessageEntity o2) {
                    if (o1.getMessageMainType() != null && o2.getMessageMainType() != null) {
                        return o1.getMessageMainType().compareTo(o2.getMessageMainType());
                    }
                    return -1;
                }
            });

            // 整理每种 messageMainType 消息的数量
            MessageUnreadCountVO messageUnreadCountVO = new MessageUnreadCountVO();
            for (OperationMessageEntity unreadMessage : unreadMessageList) {
                if (null != unreadMessage.getMessageMainType()) {
                    if (0 == messageUnreadCountVO.getMainTypeNumber()) {
                        messageUnreadCountVO.setMainTypeId(unreadMessage.getMessageMainType());
                        messageUnreadCountVO.setMainTypeName(unreadMessage.getMessageMainName());
                        messageUnreadCountVO.setMainTypeNumber(1);
                        messageUnreadCountVOList.add(messageUnreadCountVO);
                    } else if (messageUnreadCountVO.getMainTypeId().compareTo(unreadMessage.getMessageMainType()) == 0) {
                        messageUnreadCountVO.setMainTypeNumber(messageUnreadCountVO.getMainTypeNumber() + 1);
                    } else {
                        messageUnreadCountVO = new MessageUnreadCountVO();
                        messageUnreadCountVO.setMainTypeId(unreadMessage.getMessageMainType());
                        messageUnreadCountVO.setMainTypeName(unreadMessage.getMessageMainName());
                        messageUnreadCountVO.setMainTypeNumber(1);
                        messageUnreadCountVOList.add(messageUnreadCountVO);
                    }
                }
            }
        }

        responseBean.setResult(messageUnreadCountVOList);
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
        return responseBean;
    }
    /**
     * send message to user
     * include mail email and sms
     * receivers with mobiles[] and emails[]
     *
     * @return Response
     *
     */
    public ResponseBean messageReleaseWithReceivers(MessageReleaseReceiversVo messageReleaseReceiversVo){
        ResponseBean responseBean = new ResponseBean();
        List<String> emails = new ArrayList<>();
        List<String> mobiles = new ArrayList<>();
        List<String> failureList = new ArrayList<>();
        if (null == messageReleaseReceiversVo.getTemplateId()) {
            responseBean.setCode("0");
            responseBean.setMessage("模板Id不能为空");
            return responseBean;
        }
        List<MessageErrorDTO> sendFailureList=new ArrayList<>();
        String[] receivers = messageReleaseReceiversVo.getReceivers().split(",");
        for (String receiver : receivers) {
            if (CommonUtils.checkEmail(receiver)) {
                emails.add(receiver);
            } else if (CommonUtils.checkMobile(receiver)) {
                mobiles.add(receiver);
            } else {
                MessageReleaseVo messageReleaseVo = new MessageReleaseVo();
                messageReleaseVo.setTemplateId(messageReleaseReceiversVo.getTemplateId());
                messageReleaseVo.setReceiverId(receiver);
                messageReleaseVo.setEmailParams(messageReleaseReceiversVo.getEmailParams());
                messageReleaseVo.setTitleEmailParams(messageReleaseReceiversVo.getTitleEmailParams());
                messageReleaseVo.setSmsParams(messageReleaseReceiversVo.getSmsParams());
                messageReleaseVo.setTableContainerBeans(messageReleaseReceiversVo.getTableContainerBeans());
                messageReleaseVo.setFiles(messageReleaseReceiversVo.getFiles());
                ResponseBean resp=messageManageService.normalMessageRelease(messageReleaseVo,true);
                if(!org.apache.commons.lang.StringUtils.equals(resp.getCode(),"0")){
                    MessageErrorDTO messageErrorDTO=new MessageErrorDTO();
                    messageErrorDTO.setErrorMessage(resp.getMessage());
                    sendFailureList.add(messageErrorDTO);
                }
            }
        }

        if(emails.size()==0&&mobiles.size()==0){
            responseBean.setCode("1");
            responseBean.setMessage("接收人联系方式获取失败");
        }
        else if(emails.size()==0){
            for(int i=0;i<mobiles.size();i++){
                if(CommonUtils.checkMobile(mobiles.get(i))){
                    MessageReleaseVo messageReleaseVo = new MessageReleaseVo();
                    messageReleaseVo.setTemplateId(messageReleaseReceiversVo.getTemplateId());
                    messageReleaseVo.setMobile(mobiles.get(i));
                    messageReleaseVo.setEmailParams(messageReleaseReceiversVo.getEmailParams());
                    messageReleaseVo.setTitleEmailParams(messageReleaseReceiversVo.getTitleEmailParams());
                    messageReleaseVo.setSmsParams(messageReleaseReceiversVo.getSmsParams());
                    messageReleaseVo.setTableContainerBeans(messageReleaseReceiversVo.getTableContainerBeans());
                    messageReleaseVo.setFiles(messageReleaseReceiversVo.getFiles());
                    ResponseBean resp=messageManageService.abnormalMessageRelease(messageReleaseVo,true);
                    if(!org.apache.commons.lang.StringUtils.equals(resp.getCode(),"0")){
                        MessageErrorDTO messageErrorDTO=new MessageErrorDTO();
                        messageErrorDTO.setMobile(mobiles.get(i));
                        messageErrorDTO.setErrorMessage(resp.getMessage());
                        sendFailureList.add(messageErrorDTO);
                    }
                }else{
                    log.error("手机号填写错误：{}", mobiles.get(i));
                    failureList.add(mobiles.get(i));
                }
            }
        }
        else if(mobiles.size()==0){
            for(int i=0;i<emails.size();i++){
                if(CommonUtils.checkEmail(emails.get(i))){
                    MessageReleaseVo messageReleaseVo = new MessageReleaseVo();
                    messageReleaseVo.setTemplateId(messageReleaseReceiversVo.getTemplateId());
                    messageReleaseVo.setEmail(emails.get(i));
                    messageReleaseVo.setEmailParams(messageReleaseReceiversVo.getEmailParams());
                    messageReleaseVo.setTitleEmailParams(messageReleaseReceiversVo.getTitleEmailParams());
                    messageReleaseVo.setSmsParams(messageReleaseReceiversVo.getSmsParams());
                    messageReleaseVo.setTableContainerBeans(messageReleaseReceiversVo.getTableContainerBeans());
                    messageReleaseVo.setFiles(messageReleaseReceiversVo.getFiles());
                    ResponseBean resp=messageManageService.abnormalMessageRelease(messageReleaseVo,true);
                    if(!org.apache.commons.lang.StringUtils.equals(resp.getCode(),"0")){
                        MessageErrorDTO messageErrorDTO=new MessageErrorDTO();
                        messageErrorDTO.setEmail(emails.get(i));
                        messageErrorDTO.setErrorMessage(resp.getMessage());
                        sendFailureList.add(messageErrorDTO);
                    }
                }else{
                    log.error("邮箱填写错误：{}", emails.get(i));
                    failureList.add(emails.get(i));
                }
            }
        }else{
            for(int i=0;i<emails.size();i++){
                if(CommonUtils.checkEmail(emails.get(i))){
                    MessageReleaseVo messageReleaseVo = new MessageReleaseVo();
                    messageReleaseVo.setTemplateId(messageReleaseReceiversVo.getTemplateId());
                    messageReleaseVo.setEmail(emails.get(i));
                    messageReleaseVo.setSmsParams(messageReleaseReceiversVo.getSmsParams());
                    messageReleaseVo.setEmailParams(messageReleaseReceiversVo.getEmailParams());
                    messageReleaseVo.setTitleEmailParams(messageReleaseReceiversVo.getTitleEmailParams());
                    messageReleaseVo.setTableContainerBeans(messageReleaseReceiversVo.getTableContainerBeans());
                    messageReleaseVo.setFiles(messageReleaseReceiversVo.getFiles());
                    ResponseBean resp=messageManageService.abnormalMessageRelease(messageReleaseVo,true);
                    if(!org.apache.commons.lang.StringUtils.equals(resp.getCode(),"0")){
                        MessageErrorDTO messageErrorDTO=new MessageErrorDTO();
                        messageErrorDTO.setEmail(emails.get(i));
                        messageErrorDTO.setErrorMessage(resp.getMessage());
                        sendFailureList.add(messageErrorDTO);
                    }
                }else{
                    log.error("邮箱填写错误：{}", emails.get(i));
                    failureList.add(emails.get(i));
                }
            }
            for(int i=0;i<mobiles.size();i++){
                if(CommonUtils.checkMobile(mobiles.get(i))){
                    MessageReleaseVo messageReleaseVo = new MessageReleaseVo();
                    messageReleaseVo.setTemplateId(messageReleaseReceiversVo.getTemplateId());
                    messageReleaseVo.setMobile(mobiles.get(i));
                    messageReleaseVo.setSmsParams(messageReleaseReceiversVo.getSmsParams());
                    messageReleaseVo.setEmailParams(messageReleaseReceiversVo.getEmailParams());
                    messageReleaseVo.setTitleEmailParams(messageReleaseReceiversVo.getTitleEmailParams());
                    messageReleaseVo.setTableContainerBeans(messageReleaseReceiversVo.getTableContainerBeans());
                    messageReleaseVo.setFiles(messageReleaseReceiversVo.getFiles());
                    ResponseBean resp=messageManageService.abnormalMessageRelease(messageReleaseVo,true);
                    if(!org.apache.commons.lang.StringUtils.equals(resp.getCode(),"0")){
                        MessageErrorDTO messageErrorDTO=new MessageErrorDTO();
                        messageErrorDTO.setMobile(mobiles.get(i));
                        messageErrorDTO.setErrorMessage(resp.getMessage());
                        sendFailureList.add(messageErrorDTO);
                    }
                }else{
                    log.error("手机号填写错误：{}", mobiles.get(i));
                    failureList.add(mobiles.get(i));
                }
            }
        }
        if(sendFailureList.size()>0){
            responseBean.setCode("1");
            responseBean.setMessage("消息发送失败");
            responseBean.setResult(sendFailureList);
            return responseBean;
        }else{
            responseBean.setCode("0");
            responseBean.setMessage("消息发送成功");
            return responseBean;
        }

    }


    /**
     * send message to user
     * include mail email and sms
     * receiverId can not exist with mobile and email
     *
     * @return ResponseBean
     */
    public ResponseBean messageRelease(MessageReleaseVo releaseVo) {
        //params check
        if (StringUtils.isEmpty(releaseVo.getReceiverId()) && StringUtils.isEmpty(releaseVo.getEmail()) && StringUtils.isEmpty(releaseVo.getMobile())) {
            ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MESSAGE_RECEIVER_ID_NOT_EMPTY);
            throw new BusinessException(errorCodeEntity.getErrorCode(), errorCodeEntity.getErrorMessage());
        }
        if (!StringUtils.isEmpty(releaseVo.getReceiverId())) {
            //send normal message(with receiverId)
            return messageManageService.normalMessageRelease(releaseVo,true);
        } else {
            //send abnormal message(without receiverId)
            return messageManageService.abnormalMessageRelease(releaseVo,true);
        }
    }
    /**
     * send message to user
     * include mail email and sms
     * receiverId can not exist with mobile and email
     *
     * @return ResponseBean
     */
    public ResponseBean messageReleaseEntireties(MessageReleaseVo releaseVo) {

        //params check
        if (StringUtils.isEmpty(releaseVo.getReceiverId()) && StringUtils.isEmpty(releaseVo.getEmail()) && StringUtils.isEmpty(releaseVo.getMobile())) {
            ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MESSAGE_RECEIVER_ID_NOT_EMPTY);
            throw new BusinessException(errorCodeEntity.getErrorCode(), errorCodeEntity.getErrorMessage());
        }
        if (!StringUtils.isEmpty(releaseVo.getReceiverId())) {
            //send normal message(with receiverId)
            return messageManageService.normalMessageRelease(releaseVo,false);
        } else {
            //send abnormal message(without receiverId)
            return messageManageService.abnormalMessageRelease(releaseVo,false);
        }
    }

    /**
     * 发送手机验证码
     *
     * @param smsVerifyContentVo
     * @return
     */
    public ResponseBean smsVerifyCodeRelease(SmsVerifyContentVo smsVerifyContentVo) {
        ResponseBean responseBean = new ResponseBean();
        //生成验证码
        String verifyCode = CommonUtil.defaultRandomCode();
        //判断提示文本
        if(StringUtils.isEmpty(smsVerifyContentVo.getPoint())){
            smsVerifyContentVo.setPoint("");
        }
        //模板短信内容可替换信息
        String[] smsParams;
        String templateId;
        if(StringUtils.isEmpty(smsVerifyContentVo.getAction())){
            templateId=MessageConstants.MOBILE_CODE_NO_ACTION;
            smsParams = new String[]{verifyCode, (smsVerifyContentVo.getValidPeriod()).toString(),smsVerifyContentVo.getPoint()};
            //根据type_code和手机号查询消息记录
            if (checkVerifyCodeTimes(smsVerifyContentVo, responseBean, templateId)) { return responseBean;}
        }else{
            templateId=MessageConstants.MOBILE_CODE_HAS_ACTION;
            smsParams = new String[]{smsVerifyContentVo.getAction(),verifyCode, (smsVerifyContentVo.getValidPeriod()).toString(),smsVerifyContentVo.getPoint()};
            if (checkVerifyCodeTimes(smsVerifyContentVo, responseBean, templateId)) { return responseBean;}
        }
        String mobile=smsVerifyContentVo.getMobile();
        SmsVerifyCodeDTO smsVerifyCodeDTO=new SmsVerifyCodeDTO();
        BigInteger messageId;
        Date sendTime;
        MessageSendStatusVo messageSendStatusVo = new MessageSendStatusVo();
        try {
            log.info("调用消息发送接口……");
            MessageReleaseVo messageReleaseVo = new MessageReleaseVo();
            messageReleaseVo.setTemplateId( templateId);
            if (!StringUtils.isEmpty(mobile)) {
                if(CommonUtil.checkMobile(mobile)){
                    messageReleaseVo.setMobile(mobile);
                    messageReleaseVo.setSmsParams( smsParams);
                }else{
                    responseBean.setCode("000.000000400");
                    responseBean.setMessage("请输入正确的手机号");
                }
            }
            //params check
            if (StringUtils.isEmpty(messageReleaseVo.getReceiverId()) && StringUtils.isEmpty(messageReleaseVo.getEmail()) && StringUtils.isEmpty(messageReleaseVo.getMobile())) {
                ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MESSAGE_RECEIVER_ID_NOT_EMPTY);
                throw new BusinessException(errorCodeEntity.getErrorCode(), errorCodeEntity.getErrorMessage());
            }
            else {
                //send abnormal message(without receiverId)
                MessageTemplateBean messageTemplateBean = messageManageMapper.getMessageTemplateByCode(messageReleaseVo.getTemplateId());
                if (null == messageTemplateBean) {
                    log.info("send message template is not legal [{}] ", messageReleaseVo.getTemplateId());
                    ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.MESSAGE_TEMPLATE_NOT_LEGAL);
                    throw new BusinessException(errorCodeEntity.getErrorCode(), errorCodeEntity.getErrorMessage());
                }
                log.info("send message type id is {} and name is {}", messageTemplateBean.getMessageTypeId(), messageTemplateBean.getMessageTypeName());
                MessageTypeBean messageTypeBean = messageManageMapper.getMessageTypeById(messageTemplateBean.getMessageTypeId());
                if (null == messageTypeBean) {
                    log.info("send message type is not exist [{}] ", messageTemplateBean.getMessageTypeId());
                    ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.MESSAGE_TYPE_NOT_EXIST);
                    throw new BusinessException(errorCodeEntity.getErrorCode(), errorCodeEntity.getErrorMessage());
                }
                //message records
                MessageBean messageBean = messageManageService.generateMessageBean(UNKOWN, SEND_TARGET_CONTACT, messageTypeBean);
                if (!org.apache.commons.lang.StringUtils.isEmpty(messageReleaseVo.getMobile())) {
                    messageManageService.sendSmsMessage(messageTypeBean, messageTemplateBean, messageReleaseVo.getMobile(), messageReleaseVo, null, messageSendStatusVo, messageBean, true);
                }
                // 判断信息发送是否成功
                messageManageService.isSendSuccess(responseBean, messageSendStatusVo, messageBean);
                messageId=messageBean.getId();
                sendTime=messageBean.getCreatedTime();
                //保存消息
                messageManageMapper.addMessageList(messageBean);
            }
            if (!"0".equals(responseBean.getCode())) {
                log.error("消息发送失败,mobile:{},time：{}", mobile,new Date());
                throw new ApiException("004", "消息发送失败");
            }
        } catch (ApiException e) {
            log.error("消息接口调用失败，mobile:{},time：{}", mobile,new Date());
            throw new ApiException("005", "消息接口调用失败");
        }
        log.info("send message [{}] to [{}] success", Arrays.toString(smsParams) ,  mobile );
        responseBean.setCode("0");
        responseBean.setMessage("消息发送成功");
        if(smsVerifyContentVo.getValidPeriod()<0||smsVerifyContentVo.getValidPeriod()>MessageConstants.MAX_VALID_PERIOD){
            responseBean.setCode(MessageConstants.VALID_PERIOD_ERROR_CODE);
        }else{
            if (org.apache.commons.lang.StringUtils.equals(MessageConstants.STRING_ZERO,responseBean.getCode())) {
//          将验证码保存到redis中。key为手机号，设置过期时间validPeriod*60秒
                boolean i = redisUtil.set(MessageConstants.REDIS_KEY + smsVerifyContentVo.getMobile(), verifyCode,smsVerifyContentVo.getValidPeriod()*60);
                //设置验证码允许错误次数
                int errorTimes = 0;
                boolean j = redisUtil.set(MessageConstants.CODE_PERMIT_ERROR_TIMES,errorTimes);
            }
            log.info("获取手机验证码 ", "手机号：" + smsVerifyContentVo.getMobile());
            responseBean.setCode(MessageConstants.STRING_ZERO);
        }
        String status=(messageSendStatusVo.isSmsStatus()) ? "success" : "failure";
        //计算到期时间
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            //发送时间Date转换为String
            String sendReTime=sf.format(sendTime);
            //发送时间String转换为long
            long sendDate = sf.parse(sendReTime).getTime();
            long validPeriodDate = smsVerifyContentVo.getValidPeriod()*60000;
            long expiresDate = sendDate+validPeriodDate;
            //long时间转换为String
            String expiresTime=sf.format(expiresDate);
            smsVerifyCodeDTO.setSendTime(sendReTime);
            smsVerifyCodeDTO.setExpiresTime(expiresTime);
        }catch (ParseException e){
            e.printStackTrace();
        }
        smsVerifyCodeDTO.setMobile(mobile);
        smsVerifyCodeDTO.setStatus(status);
        smsVerifyCodeDTO.setMessageId(messageId);
        smsVerifyCodeDTO.setVerificationCode(verifyCode);
        smsVerifyCodeDTO.setValidPeriod(smsVerifyContentVo.getValidPeriod());
        responseBean.setResult(smsVerifyCodeDTO);
        return responseBean;
    }

    private boolean checkVerifyCodeTimes(SmsVerifyContentVo smsVerifyContentVo, ResponseBean responseBean, String templateId) {
        MessageTemplateBean messageTemplateBean = messageManageMapper.getMessageTemplateByCode(templateId);
        Map<String, Object> queryMap = new HashMap(4);
        queryMap.put("mainTypeId", "all");
        queryMap.put("messageTypeId", messageTemplateBean.getMessageTypeId());
        queryMap.put("messageTemplateType", MessageConstants.SEND_BY_SMS);
        queryMap.put("receiverContact", smsVerifyContentVo.getMobile());
        List<OperationMessageBean> operationMessageBeanList = messageManageMapper.queryHistoryMessageForPage(queryMap);
        //同一手机号发送频率限制
        if(operationMessageBeanList.size()==0){
            return false;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        try {
            Date date = new Date();
            String createdTime = sf.format(date);
            long lastedTime = sf.parse(createdTime).getTime();
            long zeroTime = sf.parse(simpleDateFormat.format(date)).getTime();
            long previous = sf.parse(sf.format(operationMessageBeanList.get(0).getCreatedTime())).getTime();
            long intervalTime = (lastedTime - previous) / 60000;
            //同一手机号发送验证码的最小时间间隔为1分钟
            if (intervalTime < 1) {
                ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.SEND_VERIFY_CODE_INTERVAL);
                responseBean.setCode(errorCodeEntity.getErrorCode());
                responseBean.setMessage(errorCodeEntity.getErrorMessage());
                return true;
            }
            //同一手机号单日发送验证码短信数量最大为10条
            int count = 0;
            for (OperationMessageBean operationMessageBean : operationMessageBeanList) {
                if (sf.parse(sf.format(operationMessageBean.getCreatedTime())).getTime() <= lastedTime && sf.parse(sf.format(operationMessageBean.getCreatedTime())).getTime() >= zeroTime) {
                    count++;
                }
            }
            if (count == MessageConstants.MAX_VERIFY_CODE_TIMES) {
                ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.SEND_VERIFY_CODE_TIMES_ERROR);
                responseBean.setCode(errorCodeEntity.getErrorCode());
                responseBean.setMessage(errorCodeEntity.getErrorMessage());
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 校验手机验证码
     *
     * @param verifyCode
     * @return
     */
    public ResponseBean smsVerifyCodeCheck(String verifyCode,String mobile) {
        ResponseBean responseBean = new ResponseBean();
        boolean flag = redisUtil.hasKey(MessageConstants.REDIS_KEY+mobile);
        if(flag){
            Object code = redisUtil.get(MessageConstants.REDIS_KEY + mobile);
            if (!org.apache.commons.lang.StringUtils.equals(verifyCode,code.toString())) {
                ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.VERIFY_CODE_ERROR);
                responseBean.setCode(errorCodeEntity.getErrorCode());
                responseBean.setMessage(errorCodeEntity.getErrorMessage());
                return  responseBean;
            }
            if (org.apache.commons.lang.StringUtils.equals(verifyCode,code.toString())) {
                responseBean.setCode("0");
                responseBean.setMessage("验证码正确。");
                return responseBean;
            }
        }else{
            ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.VERIFY_CODE_INVALID);
            responseBean.setCode(errorCodeEntity.getErrorCode());
            responseBean.setMessage(errorCodeEntity.getErrorMessage());
            return responseBean;
        }
        return responseBean;
    }

    /**
     * Group send email
     * @param messageReleaseReceiversVo
     * @return
     */
    public ResponseBean messageGroupRelease(MessageReleaseReceiversVo messageReleaseReceiversVo) {
        ResponseBean responseBean = new ResponseBean();
        List<String> emails = new ArrayList<>();
        List<MessageErrorDTO> sendFailureList = new ArrayList<>();
        List<String> failureList = new ArrayList<>();
        if (null == messageReleaseReceiversVo.getTemplateId()) {
            responseBean.setCode("0");
            responseBean.setMessage("模板Id不能为空");
            return responseBean;
        }
        String[] receivers = messageReleaseReceiversVo.getReceivers().split(",");
        for (String receiver : receivers) {
            if (CommonUtils.checkEmail(receiver)) {
                emails.add(receiver);
            }else{
                failureList.add(receiver);
            }
        }
        if(emails.size()==0){
            responseBean.setCode("1");
            responseBean.setMessage("接收人邮箱不能为空");
        } else {
            MessageReleaseVo messageReleaseVo = new MessageReleaseVo();
            messageReleaseVo.setTemplateId(messageReleaseReceiversVo.getTemplateId());
            messageReleaseVo.setEmail(org.apache.commons.lang.StringUtils.join(emails.toArray(),","));
            messageReleaseVo.setEmailParams(messageReleaseReceiversVo.getEmailParams());
            messageReleaseVo.setTitleEmailParams(messageReleaseReceiversVo.getTitleEmailParams());
            messageReleaseVo.setTableContainerBeans(messageReleaseReceiversVo.getTableContainerBeans());
            messageReleaseVo.setFiles(messageReleaseReceiversVo.getFiles());
            MessageTemplateBean messageTemplateBean = messageManageMapper.getMessageTemplateByCode(messageReleaseVo.getTemplateId());
            if (null == messageTemplateBean) {
                log.info("send message template is not legal [{}] ", messageReleaseVo.getTemplateId());
                ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.MESSAGE_TEMPLATE_NOT_LEGAL);
                throw new BusinessException(errorCodeEntity.getErrorCode(), errorCodeEntity.getErrorMessage());
            }
            log.info("send message type id is {} and name is {}", messageTemplateBean.getMessageTypeId(), messageTemplateBean.getMessageTypeName());
            MessageTypeBean messageTypeBean = messageManageMapper.getMessageTypeById(messageTemplateBean.getMessageTypeId());
            if (null == messageTypeBean) {
                log.info("send message type is not exist [{}] ", messageTemplateBean.getMessageTypeId());
                ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.MESSAGE_TYPE_NOT_EXIST);
                throw new BusinessException(errorCodeEntity.getErrorCode(), errorCodeEntity.getErrorMessage());
            }
            /** check contact and  send message*/
            MessageSendStatusVo messageSendStatusVo = new MessageSendStatusVo();
            //message records
            MessageBean messageBean = messageManageService.generateMessageBean(UNKOWN, SEND_TARGET_CONTACT, messageTypeBean);
            if (!org.apache.commons.lang.StringUtils.isEmpty(messageReleaseVo.getEmail())) {
                try {
                    String messageContent = messageManageService.generateMessageContent(messageTemplateBean.getTemplateContentEmail(), messageReleaseVo.getEmailParams(), messageReleaseVo.getTableContainerBeans(), true);
                    String titleMessageContent = messageManageService.generateMessageContent(messageTemplateBean.getTemplateTitleEmail(), messageReleaseVo.getTitleEmailParams(), messageReleaseVo.getTableContainerBeans(), true);
                    log.info("send {} message of email,content is:{}",messageReleaseVo.getEmail() , messageContent);
                    String hasAttachment = (messageReleaseVo.getFiles() != null && messageReleaseVo.getFiles().size() > 0) ? "1" : "0";
                    //判断消息系统是否启用
                    CommonConfigBean commonConfigBean = commonConfig.getConfig(MessageConstants.CURRENT_SWITCH_COMMON_CONFIG_NAME,MessageConstants.CURRENT_SUPPLIER_COMMON_CONFIG_GROUPNAME);
                    if(org.apache.commons.lang.StringUtils.equals(commonConfigBean.getValue().toUpperCase(),MessageConstants.CURRENT_MESSAGE_SYSTEM_STATUS)){
                        log.info("消息系统此时的状态：{}",commonConfigBean.getValue());
                        BigInteger commonMessageId = messageManageService.saveMessageBeanToCommonMsg(messageContent,"EMAIL");
                        OperationMessageEntity operationMessageEntity = messageManageService.generateMessageBean("email", messageTemplateBean.getTemplateTitleEmail(), messageContent, messageReleaseVo.getEmail(), null, messageTypeBean, false, messageBean, commonMessageId);
                        messageManageMapper.saveMessageRecords(operationMessageEntity);
                        ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.MESSAGE_SYSTEM_STATUS_OFF);
                        throw new BusinessException(errorCodeEntity.getErrorCode(),errorCodeEntity.getErrorMessage());
                    }
                    Integer priority ;
                    priority = messageManageService.getMessageTypePriority(messageTypeBean);
                    BigInteger commonMessageId = messageManageService.sendEmailMessageForward(messageReleaseVo.getEmail(), titleMessageContent, messageContent, hasAttachment, priority);
                    boolean success = commonMessageId != null;
                    User loginUser = SecurityContextUtil.getLoginUser();
                    if (success) {
                        //保存附件信息
                        if (messageReleaseVo.getFiles() != null && messageReleaseVo.getFiles().size() > 0) {
                            for (FileBean fileBean : messageReleaseVo.getFiles()) {
                                try {
                                    fileBean.setModuleId(commonMessageId.toString());
                                    OperationAttachmentInfo operationAttachmentInfo = operationAttachmentInfoService.groupOperation(OperationAttachmentInfoService.OPERATION_WORKORDER_MESSAGE, fileBean);
                                    operationAttachmentInfo.setId(new BigInteger(String.valueOf(IdWorker.getNextId())));
                                    operationAttachmentInfo.setCreatedTime(new Date());
                                    if (loginUser != null) {
                                        operationAttachmentInfo.setCreatedUserName(loginUser.getName());
                                        operationAttachmentInfo.setCreatedUserId(loginUser.getId());
                                    }
                                    operationAttachmentInfo.setTableName("common_user_message");
                                    operationAttachmentInfoService.saveOperationAttachment(operationAttachmentInfo);
                                } catch (Exception e) {
                                    log.error("MessageManageService-sendEmailMessageWithFiles :save message error");
                                    throw e;
                                }
                            }
                        }
                    }
                    for(String contact:emails){
                        OperationMessageEntity operationMessageEntity = messageManageService.generateMessageBean("email", messageTemplateBean.getTemplateTitleEmail(), messageContent, contact, null, messageTypeBean, success, messageBean, commonMessageId);
                        messageManageMapper.saveMessageRecords(operationMessageEntity);
                    }
                    messageBean.setEmailContent(messageContent);
                    messageBean.setEmailSubject(titleMessageContent);
                    messageBean.setSendEmailMessage(OPERATION_YES);
                    messageSendStatusVo.setEmailStatus(success);
                    messageSendStatusVo.setEmailStatusDesc(success ? EMAIL_SEND_SUCCESS : EMAIL_SEND_FAILURE);
                } catch (Exception e) {
                    messageSendStatusVo.setEmailStatus(false);
                    messageSendStatusVo.setEmailStatusDesc(EMAIL_SEND_FAILURE+e);
                    log.error("ERROR", "fail to send email ", e);
                }
            }
            /** 判断信息发送是否成功*/
            if (messageSendStatusVo.isMailStatus() && messageSendStatusVo.isEmailStatus() && messageSendStatusVo.isSmsStatus()) {
                messageBean.setStatus(SUCCESS_SEND_MESSAGE);
            } else {
                ErrorCodeEntity codeEntity = messageErrorCode.getMessageErrorMsg(messageErrorCode.MESSAGE_SEND_FAILURE);
                responseBean.setCode(codeEntity.getErrorCode());
                responseBean.setMessage(codeEntity.getErrorMessage());
                messageBean.setStatus(FAILURE_SEND_MESSAGE);
            }
            //保存消息
            messageManageMapper.addMessageList(messageBean);
            if (org.apache.commons.lang.StringUtils.equals(messageBean.getStatus(),FAILURE_SEND_MESSAGE)) {
                MessageErrorDTO messageErrorDTO = new MessageErrorDTO();
                messageErrorDTO.setEmail(org.apache.commons.lang.StringUtils.join(emails.toArray(),","));
                messageErrorDTO.setErrorMessage(responseBean.getMessage());
                sendFailureList.add(messageErrorDTO);
            }
        }
        if(sendFailureList.size()>0){
            responseBean.setCode(ResponseCode.RESPONSE_CODE_FAILURE);
            responseBean.setMessage("消息发送失败");
            responseBean.setResult(sendFailureList);
            return responseBean;
        }else if(failureList.size()>0){
            responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
            responseBean.setMessage("部分邮箱不存在发送失败");
            responseBean.setResult("失败的邮箱"+org.apache.commons.lang.StringUtils.join(failureList.toArray(),","));
            return responseBean;
        }else{
            responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
            responseBean.setMessage("消息发送成功");
            return responseBean;
        }
    }

    /**
     * 自定义发件人邮件群发
     * @param messageReleaseSenderEmailVO
     * @return
     */
    public ResponseBean messageSenderEmailRelease(MessageReleaseSenderEmailVO messageReleaseSenderEmailVO) {
        ResponseBean responseBean = new ResponseBean();
        List<String> emails = new ArrayList<>();
        List<MessageErrorDTO> sendFailureList = new ArrayList<>();
        List<String> failureList = new ArrayList<>();
        String senderEmail = messageReleaseSenderEmailVO.getSenderEmail();
        String password = messageReleaseSenderEmailVO.getEmailPassword();
        String nickName = messageReleaseSenderEmailVO.getNickName();
        if (null == messageReleaseSenderEmailVO.getTemplateId()) {
            responseBean.setCode("0");
            responseBean.setMessage("模板Id不能为空");
            return responseBean;
        }
        String[] receivers = messageReleaseSenderEmailVO.getReceivers().split(",");
        for (String receiver : receivers) {
            if (CommonUtils.checkEmail(receiver)) {
                emails.add(receiver);
            }else{
                failureList.add(receiver);
            }
        }
        if(emails.size()==0){
            responseBean.setCode("1");
            responseBean.setMessage("接收人邮箱不能为空");
        } else {
            MessageReleaseVo messageReleaseVo = new MessageReleaseVo();
            messageReleaseVo.setTemplateId(messageReleaseSenderEmailVO.getTemplateId());
            messageReleaseVo.setEmail(org.apache.commons.lang.StringUtils.join(emails.toArray(),","));
            messageReleaseVo.setEmailParams(messageReleaseSenderEmailVO.getEmailParams());
            messageReleaseVo.setTitleEmailParams(messageReleaseSenderEmailVO.getTitleEmailParams());
            messageReleaseVo.setTableContainerBeans(messageReleaseSenderEmailVO.getTableContainerBeans());
            messageReleaseVo.setFiles(messageReleaseSenderEmailVO.getFiles());
            MessageTemplateBean messageTemplateBean = messageManageMapper.getMessageTemplateByCode(messageReleaseVo.getTemplateId());
            if (null == messageTemplateBean) {
                log.info("send message template is not legal [{}] ", messageReleaseVo.getTemplateId());
                ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.MESSAGE_TEMPLATE_NOT_LEGAL);
                throw new BusinessException(errorCodeEntity.getErrorCode(), errorCodeEntity.getErrorMessage());
            }
            log.info("send message type id is {} and name is {}", messageTemplateBean.getMessageTypeId(), messageTemplateBean.getMessageTypeName());
            MessageTypeBean messageTypeBean = messageManageMapper.getMessageTypeById(messageTemplateBean.getMessageTypeId());
            if (null == messageTypeBean) {
                log.info("send message type is not exist [{}] ", messageTemplateBean.getMessageTypeId());
                ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.MESSAGE_TYPE_NOT_EXIST);
                throw new BusinessException(errorCodeEntity.getErrorCode(), errorCodeEntity.getErrorMessage());
            }
            /** check contact and  send message*/
            MessageSendStatusVo messageSendStatusVo = new MessageSendStatusVo();
            //message records
            MessageBean messageBean = messageManageService.generateMessageBean(UNKOWN, SEND_TARGET_CONTACT, messageTypeBean);
            if (!org.apache.commons.lang.StringUtils.isEmpty(messageReleaseVo.getEmail())) {
                try {
                    String messageContent = messageManageService.generateMessageContent(messageTemplateBean.getTemplateContentEmail(), messageReleaseVo.getEmailParams(), messageReleaseVo.getTableContainerBeans(), true);
                    String titleMessageContent = messageManageService.generateMessageContent(messageTemplateBean.getTemplateTitleEmail(), messageReleaseVo.getTitleEmailParams(), messageReleaseVo.getTableContainerBeans(), true);
                    log.info("send {} message of email,content is:{}",messageReleaseVo.getEmail() , messageContent);
                    String hasAttachment = (messageReleaseVo.getFiles() != null && messageReleaseVo.getFiles().size() > 0) ? "1" : "0";
                    //判断消息系统是否启用
                    CommonConfigBean commonConfigBean = commonConfig.getConfig(MessageConstants.CURRENT_SWITCH_COMMON_CONFIG_NAME,MessageConstants.CURRENT_SUPPLIER_COMMON_CONFIG_GROUPNAME);
                    if(org.apache.commons.lang.StringUtils.equals(commonConfigBean.getValue().toUpperCase(),MessageConstants.CURRENT_MESSAGE_SYSTEM_STATUS)){
                        log.info("消息系统此时的状态：{}",commonConfigBean.getValue());
                        BigInteger commonMessageId = messageManageService.saveMessageBeanToCommonMsg(messageContent,"EMAIL");
                        OperationMessageEntity operationMessageEntity = messageManageService.generateMessageBean("email", messageTemplateBean.getTemplateTitleEmail(), messageContent, messageReleaseVo.getEmail(), null, messageTypeBean, false, messageBean, commonMessageId);
                        messageManageMapper.saveMessageRecords(operationMessageEntity);
                        ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.MESSAGE_SYSTEM_STATUS_OFF);
                        throw new BusinessException(errorCodeEntity.getErrorCode(),errorCodeEntity.getErrorMessage());
                    }
                    Integer priority ;
                    priority = messageManageService.getMessageTypePriority(messageTypeBean);
                    BigInteger commonMessageId = messageManageService.sendEmailMessageBySenderEmailForward(messageReleaseVo.getEmail(), titleMessageContent, messageContent, hasAttachment, priority,senderEmail,password,nickName);
                    boolean success = commonMessageId != null;
                    User loginUser = SecurityContextUtil.getLoginUser();
                    if (success) {
                        //保存附件信息
                        if (messageReleaseVo.getFiles() != null && messageReleaseVo.getFiles().size() > 0) {
                            for (FileBean fileBean : messageReleaseVo.getFiles()) {
                                try {
                                    fileBean.setModuleId(commonMessageId.toString());
                                    OperationAttachmentInfo operationAttachmentInfo = operationAttachmentInfoService.groupOperation(OperationAttachmentInfoService.OPERATION_WORKORDER_MESSAGE, fileBean);
                                    operationAttachmentInfo.setId(new BigInteger(String.valueOf(IdWorker.getNextId())));
                                    operationAttachmentInfo.setCreatedTime(new Date());
                                    if (loginUser != null) {
                                        operationAttachmentInfo.setCreatedUserName(loginUser.getName());
                                        operationAttachmentInfo.setCreatedUserId(loginUser.getId());
                                    }
                                    operationAttachmentInfo.setTableName("common_user_message");
                                    operationAttachmentInfoService.saveOperationAttachment(operationAttachmentInfo);
                                } catch (Exception e) {
                                    log.error("MessageManageService-sendEmailMessageWithFiles :save message error");
                                    throw e;
                                }
                            }
                        }
                    }
                    for(String contact:emails){
                        OperationMessageEntity operationMessageEntity = messageManageService.generateMessageBean("email", messageTemplateBean.getTemplateTitleEmail(), messageContent, contact, null, messageTypeBean, success, messageBean, commonMessageId);
                        messageManageMapper.saveMessageRecords(operationMessageEntity);
                    }
                    messageBean.setEmailContent(messageContent);
                    messageBean.setEmailSubject(titleMessageContent);
                    messageBean.setSendEmailMessage(OPERATION_YES);
                    messageSendStatusVo.setEmailStatus(success);
                    messageSendStatusVo.setEmailStatusDesc(success ? EMAIL_SEND_SUCCESS : EMAIL_SEND_FAILURE);
                } catch (Exception e) {
                    messageSendStatusVo.setEmailStatus(false);
                    messageSendStatusVo.setEmailStatusDesc(EMAIL_SEND_FAILURE+e);
                    log.error("ERROR", "fail to send email ", e);
                }
            }
            /** 判断信息发送是否成功*/
            if (messageSendStatusVo.isMailStatus() && messageSendStatusVo.isEmailStatus() && messageSendStatusVo.isSmsStatus()) {
                messageBean.setStatus(SUCCESS_SEND_MESSAGE);
            } else {
                ErrorCodeEntity codeEntity = messageErrorCode.getMessageErrorMsg(messageErrorCode.MESSAGE_SEND_FAILURE);
                responseBean.setCode(codeEntity.getErrorCode());
                responseBean.setMessage(codeEntity.getErrorMessage());
                messageBean.setStatus(FAILURE_SEND_MESSAGE);
            }
            //保存消息
            messageManageMapper.addMessageList(messageBean);
            if (org.apache.commons.lang.StringUtils.equals(messageBean.getStatus(),FAILURE_SEND_MESSAGE)) {
                MessageErrorDTO messageErrorDTO = new MessageErrorDTO();
                messageErrorDTO.setEmail(org.apache.commons.lang.StringUtils.join(emails.toArray(),","));
                messageErrorDTO.setErrorMessage(responseBean.getMessage());
                sendFailureList.add(messageErrorDTO);
            }
        }
       /* boolean flag = true;
        while (flag){
            //确定没有正在发送的邮件
            int count = operationMessageMapper.getSuccessEmail(senderEmail);
            if(count==0){
                flag = false;
            }
        }*/
        if(sendFailureList.size()>0){
            responseBean.setCode(ResponseCode.RESPONSE_CODE_FAILURE);
            responseBean.setMessage("消息发送失败");
            responseBean.setResult(sendFailureList);
            return responseBean;
        }else if(failureList.size()>0){
            responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
            responseBean.setMessage("部分邮箱不存在发送失败");
            responseBean.setResult("失败的邮箱"+org.apache.commons.lang.StringUtils.join(failureList.toArray(),","));
            return responseBean;
        }else{
            responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
            responseBean.setMessage("消息发送成功");
            return responseBean;
        }
    }
}
