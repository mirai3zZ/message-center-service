package com.yangyang.cloud.message.service;

import com.inspur.bss.commonsdk.utils.IdWorker;
import com.yangyang.cloud.common.*;
import com.yangyang.cloud.common.bean.CommonConfigBean;
import com.yangyang.cloud.common.bean.User;
import com.yangyang.cloud.common.bean.UserInfoBean;
import com.yangyang.cloud.common.enums.MessageTemplateTypeEnum;
import com.yangyang.cloud.common.exception.ApiException;
import com.yangyang.cloud.common.exception.BusinessException;
import com.yangyang.cloud.common.exception.model.ErrorCodeEntity;
import com.yangyang.cloud.common.paginate.Page;
import com.yangyang.cloud.keycloak.SecurityContextUtil;
import com.yangyang.cloud.message.bean.*;
import com.yangyang.cloud.message.common.MessageConstants;
import com.yangyang.cloud.message.common.MessageErrorCode;
import com.yangyang.cloud.message.dto.MessageDeleteDTO;
import com.yangyang.cloud.message.mapper.OperationMessageListMapper;
import com.yangyang.cloud.message.mapper.OperationMessageMapper;
import com.yangyang.cloud.message.mapper.OperationMessageTypeMapper;
import com.yangyang.cloud.message.mapper.UserMessageMapper;
import com.yangyang.cloud.message.vo.*;
import com.yangyang.cloud.messagemanage.bean.MessageTypeBean;
import com.yangyang.cloud.messagemanage.mapper.MessageManageMapper;
import com.yangyang.cloud.messagemanage.service.MessageManageService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

/**
 * @author: mengfanlong
 * E-mail: meng.fanlong@inspur.com
 * Date:   2018/9/12 23:51
 * -------------------------------
 * Desc:   用户数据处理
 */
@Service
@Slf4j
public class UserMessageService {
    private static final String MARK_ALL_READ = "all";
    private static final String MAIL_SEND_SETTING_N = "N";
    private static final String MAIL_SEND_SETTING_Y = "Y";
    @Autowired
    private UserMessageMapper userMessageMapper;
    @Autowired
    private MessageManageMapper messageManageMapper;
    @Autowired
    private MessageManageService messageManageService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private OperationMessageListMapper operationMessageListMapper;
    @Autowired
    private OperationMessageMapper operationMessageMapper;
    @Autowired
    private OperationMessageTypeMapper operationMessageTypeMapper;
    @Autowired
    private MessageErrorCode messageErrorCode;

    /**发送短信或邮件*/
    @Autowired
    private MessageService messageRelease;

    /**
     * 功能描述: 通过userId获取用户消息接收配置信息
     *
     * @return List<UserMessageSettingEntity>
     * @author chenxinyi
     * @date 2018/9/29 10:13
     */
    public ResponseBean getUserSetting() {
        ResponseBean responseBean = new ResponseBean();
        User user = SecurityContextUtil.getLoginUser();
        String userId = user.getId();
        List<UserMessageSettingEntity> userSettingList = userMessageMapper.getUserMessageSetting(userId);
        List<OperationMessageTypeEntity> defaultList = userMessageMapper.getDefaultMessageSetting();
        List<UserMessageSettingEntity> addUserSettingList = new ArrayList<>();
        List<UserMessageReceiverSettingEntity> umrsList = new ArrayList<>();
        List<UserMessageReceiverSettingEntity> addUmrsList = new ArrayList<>();
        BigInteger receiverId=addUserMessageReceiver(userId).getId();
        if (0 == userSettingList.size()) {
            //添加默认配置
            addDefaultSetting(defaultList, userId, userSettingList);

            for (UserMessageSettingEntity userMes : userSettingList) {
                //将自己作接收人更新到关联表
                umrsList.add(assembleUserMessReceiverSettingEntity(userMes.getId(), userId,receiverId));
            }
            userMessageMapper.addMessageReceiverSetting(umrsList);
        } else {
            //只判断增加的情况
            boolean flag;

            for (OperationMessageTypeEntity omte : defaultList) {
                flag = false;
                for (UserMessageSettingEntity userMes : userSettingList) {
                    if (0 == omte.getId().compareTo(userMes.getMessageTypeId())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    UserMessageSettingEntity userMessageSettingEntity = getUserMessSetByOperMessType(userId, omte);
                    addUserSettingList.add(userMessageSettingEntity);
                    addUmrsList.add(assembleUserMessReceiverSettingEntity(userMessageSettingEntity.getId(), userId,receiverId));
                }
            }
            if (!addUserSettingList.isEmpty()) {
                userSettingList.addAll(addUserSettingList);
                userMessageMapper.addUserMessageSetting(addUserSettingList);
                userMessageMapper.addMessageReceiverSetting(addUmrsList);
            }
        }
        for (UserMessageSettingEntity userMes : userSettingList) {
            //返回值增加接收人id和接收人姓名列表
            userMes.setData(userMessageMapper.getMessageReceiverInfo(userMes.getId() + "", userId));
        }
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
        responseBean.setResult(userSettingList);
        return responseBean;
    }

    /**
     *description:将账号联系人更新到接收人列表
     *@author: LiuYang01
     *@date: 2019/10/9 10:05
     */
    private UserMessageAddVo addUserMessageReceiver(String userId){
        UserMessageAddVo userMessageAddVo = new UserMessageAddVo();
        UserBaseInfoEntity userBaseInfoEntity = messageManageMapper.getUserBean(userId);
        userMessageAddVo.setIsUserSelf(MessageConstants.STATUS_YES);
        userMessageAddVo.setId(BigInteger.valueOf(IdWorker.getNextId()));
        userMessageAddVo.setCreatedTime(new Date());
        userMessageAddVo.setCreatedUserId(userId);
        userMessageAddVo.setReceiverName(MessageConstants.RECEIVER_SELF);
        userMessageAddVo.setReceiverEmail(userBaseInfoEntity.getEmail()==null?" ":userBaseInfoEntity.getEmail());
        userMessageAddVo.setReceiverMobile(userBaseInfoEntity.getMobile()==null?" ":userBaseInfoEntity.getMobile());
        userMessageAddVo.setEmailCheckPass(MessageConstants.STATUS_YES);
        userMessageAddVo.setMobileCheckPass(MessageConstants.STATUS_YES);
        userMessageAddVo.setStatus(MessageConstants.STATUS_YES);
        return userMessageAddVo;
    }
    /**
     * desc: 增加默认配置
     *
     * @param defaultList List<OperationMessageTypeEntity>
     * @author chenxinyi
     * @date 2018/11/15 14:28
     */
    private void addDefaultSetting(List<OperationMessageTypeEntity> defaultList, String userId, List<UserMessageSettingEntity> userSettingList) {
        for (OperationMessageTypeEntity omte : defaultList) {
            userSettingList.add(getUserMessSetByOperMessType(userId, omte));
        }
        userMessageMapper.addUserMessageSetting(userSettingList);
    }

    /**
     * desc:Get UserMessageSettingEntity according to OperationMessageTypeEntity
     *
     * @param userId Current account id
     * @param omte   OperationMessageTypeEntity
     * @return UserMessageSettingEntity
     * @author chenxinyi
     * @date 2018/11/6 9:59
     */
    private UserMessageSettingEntity getUserMessSetByOperMessType(String userId, OperationMessageTypeEntity omte) {
        UserMessageSettingEntity userMes = new UserMessageSettingEntity();
        userMes.setId(new BigInteger(IdWorker.getNextId() + ""));
        userMes.setCreatedTime(new Date());
        userMes.setCreatedUserId(userId);
        userMes.setMainTypeId(omte.getParentId());
        userMes.setMainTypeName(omte.getParentName());
        userMes.setMessageTypeId(omte.getId());
        userMes.setMessageTypeName(omte.getTypeName());
        userMes.setSendEmailSetting(omte.getSendEmailDefaultSetting());
        userMes.setSendMailSetting(omte.getSendMailDefaultSetting());
        userMes.setSendMobileSetting(omte.getSendMobileDefaultSetting());
        userMes.setCanEdit(omte.getCanEdit());
        userMes.setStatus(MessageConstants.STATUS_YES);
        return userMes;
    }

    /**
     * desc: Assemble UserMessageReceiverSettingEntity
     *
     * @param mseeageSettingId BigInteger
     * @return userId user id
     * @author chenxinyi
     * @date 2018/11/6 17:34
     */
    private UserMessageReceiverSettingEntity assembleUserMessReceiverSettingEntity(BigInteger mseeageSettingId, String userId,BigInteger receiverId) {
        UserMessageReceiverSettingEntity umrs = new UserMessageReceiverSettingEntity();
        umrs.setId(new BigInteger(IdWorker.getNextId() + ""));
        umrs.setMessageSettingId(mseeageSettingId);
        umrs.setCreatedUserId(userId);
        umrs.setCreatedTime(new Date());
        umrs.setReceiverId(receiverId);
        umrs.setStatus(MessageConstants.STATUS_YES);
        return umrs;
    }

    /**
     * desc: Batch update user information receiving settings
     *
     * @param userMessageSettingEntities UserMessageSettingEntity[]
     * @return ResponseBean
     * @author chenxinyi
     * @date 2018/9/29 10:13
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean editUserSetting(List<UserMessageSettingEntity> userMessageSettingEntities) {
        ResponseBean responseBean = new ResponseBean();
        List<UserMessageSettingEntity> newList = new ArrayList<>();
        for (UserMessageSettingEntity userMessageSettingEntity : userMessageSettingEntities) {
            //根据id获取can_edit
            String getCanEdit = userMessageMapper.getCanEditById(userMessageSettingEntity.getId());

            if (MessageConstants.STATUS_YES.equals(getCanEdit)) {
                userMessageSettingEntity.setCanEdit(MessageConstants.STATUS_YES);
                userMessageSettingEntity.setUpdatedTime(new Date());
                newList.add(userMessageSettingEntity);
            }
        }
        if (newList.size() > 0) {
            int result = userMessageMapper.editUserMessageSetting(newList);
            if (1 == result) {
                responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
                responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
            } else {
                responseBean.setCode(ResponseCode.RESPONSE_CODE_FAILURE);
                responseBean.setMessage("设置失败");
            }
        } else {
            responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
            responseBean.setMessage("不可编辑此设置");
        }
        return responseBean;
    }

    /**
     * 功能描述: 修改用户消息接收人接口
     *
     * @param userMessageReceiverSettingVo UserMessageReceiverSettingVo
     * @return ResponseBean
     * @author chenxinyi
     * @date 2018/9/29 10:13
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean editUserReceiver(UserMessageReceiverSettingVo userMessageReceiverSettingVo) {
        ResponseBean responseBean = new ResponseBean();
        User user = SecurityContextUtil.getLoginUser();
        String userId = user.getId();
        List<UserMessageReceiverSettingEntity> list = Arrays.asList(userMessageReceiverSettingVo.getList());
        BigInteger messageSettingId = userMessageReceiverSettingVo.getId();
        List<UserMessageReceiverSettingEntity> addList = new ArrayList<>();
        for (UserMessageReceiverSettingEntity umrs : list) {
            UserMessageReceiverSettingEntity usermrse = userMessageMapper.getAllMessageReceiver(messageSettingId, umrs.getId());
            if (null == usermrse) {
                usermrse = assembleUserMessRecSetting(userId, messageSettingId, umrs.getId(), umrs.getStatus());
                addList.add(usermrse);
            }
            umrs.setReceiverId(umrs.getId());
            umrs.setCreatedUserId(userId);
            umrs.setMessageSettingId(messageSettingId);
            umrs.setUpdatedTime(new Date());
        }
        //批量更新没有接收人的情况
        if (!addList.isEmpty()) {
            userMessageMapper.addMessageReceiverSetting(addList);
        }
        //批量更新接收人接受状态
        int result = userMessageMapper.updateMessageReceiverSetting(list);
        if (1 == result) {
            responseBean.setCode("0");
            responseBean.setMessage("更新成功");
        } else {
            responseBean.setCode("2");
            responseBean.setMessage("更新失败");
        }
        return responseBean;
    }

    /**
     * desc: 组装UserMessageReceiverSettingEntity
     *
     * @param userId String
     * @return usermrse UserMessageReceiverSettingEntity
     * @author chenxinyi
     * @date 2018/11/15 14:43
     */
    private UserMessageReceiverSettingEntity assembleUserMessRecSetting(String userId, BigInteger messageSettingId, BigInteger id, String status) {
        UserMessageReceiverSettingEntity usermrse = new UserMessageReceiverSettingEntity();
        usermrse.setId(new BigInteger(IdWorker.getNextId() + ""));
        usermrse.setCreatedTime(new Date());
        usermrse.setCreatedUserId(userId);
        usermrse.setMessageSettingId(messageSettingId);
        usermrse.setReceiverId(id);
        usermrse.setStatus(status);
        return usermrse;
    }
    /**
     * 功能描述: 用户消息接收恢复默认设置接口
     *
     * @return ResponseBean
     * @author chenxinyi
     * @date 2018/9/29 10:13
     */
    @Transactional(rollbackFor = Exception.class)
    public List<UserMessageSettingEntity> resetUserReceiverSetting() {
        User user = SecurityContextUtil.getLoginUser();
        List<OperationMessageTypeEntity> defaultList = userMessageMapper.getDefaultMessageSetting();
        List<UserMessageSettingEntity> userMessageSettingList = userMessageMapper.getUserMessageSetting(user.getId());
        for (OperationMessageTypeEntity omte : defaultList) {
            for (UserMessageSettingEntity userMes : userMessageSettingList) {
                if (0 == omte.getId().compareTo(userMes.getMessageTypeId())) {
                    userMes.setSendEmailSetting(omte.getSendEmailDefaultSetting());
                    userMes.setSendMailSetting(omte.getSendMailDefaultSetting());
                    userMes.setSendMobileSetting(omte.getSendMobileDefaultSetting());
                    userMes.setUpdatedTime(new Date());
                    userMes.setCanEdit(omte.getCanEdit());
                }
            }
        }
        userMessageMapper.editUserMessageSetting(userMessageSettingList);
        for (UserMessageSettingEntity userMes : userMessageSettingList) {
            //返回值增加接收人id和接收人姓名列表
            userMes.setData(userMessageMapper.getMessageReceiverInfo(userMes.getId() + "", user.getId()));
        }
        return userMessageSettingList;
    }

    /**
     * 功能描述: 手机号或邮箱验证接收人接口
     *
     * @return ResponseBean
     * @author chenxinyi
     * @date 2018/9/29 10:13
     */
    public ResponseBean verifyReceiverMessage(String token) {
        token = token.trim();
        ResponseBean responseBean = new ResponseBean();
        String aesToken = AESCrypt.decrypt(token, MessageConstants.USER_MESSAGE_ACTIVITE_PASS);
        if (StringUtils.isEmpty(aesToken)) {
            responseBean.setCode("1");
            responseBean.setMessage("验证失败");
            return responseBean;
        }
        String[] verifyInfo = aesToken.split("_");
        //验证时间，一天
        if (MessageConstants.ONE_DAY_TIME_SPAN < System.currentTimeMillis() - Long.parseLong(verifyInfo[3])) {
            responseBean.setCode("2");
            responseBean.setMessage("链接失效，请重新获取");
            return responseBean;
        }
        UserMessageReceiverEntity userMessageReceiverEntity = userMessageMapper.getUserReceiver(new BigInteger(verifyInfo[1]));
        if (null == userMessageReceiverEntity) {
            responseBean.setCode("3");
            responseBean.setMessage("激活失败，没有该用户");
            return responseBean;
        }
        if (MessageTemplateTypeEnum.EMAIL.getType().equals(verifyInfo[2].toLowerCase())) {
            userMessageReceiverEntity.setEmailCheckPass("Y");
        } else {
            userMessageReceiverEntity.setMobileCheckPass("Y");
        }
        userMessageMapper.updateMessageReceiver(userMessageReceiverEntity);
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setMessage("验证成功");
        return responseBean;
    }

    /**
     * invite receivers
     *
     * @param receiverId String
     * @param mobile     String
     * @param email      String
     * @return ResponseBean
     */
    public ResponseBean receiverInvite(String receiverId, String mobile, String email) {
        ResponseBean responseBean = new ResponseBean();
        User user = SecurityContextUtil.getLoginUser();
        String url = commonConfig.getConfig(MessageConstants.COMMON_CONFIG_RECEIVER_INVITATION_URL, MessageConstants.COMMON_CONFIG_GROUP).getValue();
        String templateId = commonConfig.getConfig(MessageConstants.COMMON_CONFIG_RECEIVER_INVITATION_TEMPLATE_ID, MessageConstants.COMMON_CONFIG_GROUP).getValue();
        MessageReleaseVo messageReleaseVo = new MessageReleaseVo();
        messageReleaseVo.setTemplateId(templateId);
        String contact = getContactInfo();
        //判断消息系统是否启用
        CommonConfigBean commonConfigBean = commonConfig.getConfig(MessageConstants.CURRENT_SWITCH_COMMON_CONFIG_NAME,MessageConstants.CURRENT_SUPPLIER_COMMON_CONFIG_GROUPNAME);
        try{
            if(org.apache.commons.lang.StringUtils.equals(commonConfigBean.getValue().toUpperCase(),MessageConstants.CURRENT_MESSAGE_SYSTEM_STATUS)){
                log.info("消息系统此时的状态：{}",commonConfigBean.getValue());
                ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.MESSAGE_SYSTEM_STATUS_OFF);
                throw new BusinessException(errorCodeEntity.getErrorCode(),errorCodeEntity.getErrorMessage());
            }
        }catch(Exception e){
            log.error("ERROR", "fail to send message ", e);
        }
        if (!StringUtils.isEmpty(mobile)&&!org.apache.commons.lang.StringUtils.equals(commonConfigBean.getValue().toUpperCase(),MessageConstants.CURRENT_MESSAGE_SYSTEM_STATUS)) {
            String aesTokenSms = AESCrypt.encrypt(user.getId() + "_" + receiverId + "_SMS_" + System.currentTimeMillis(), MessageConstants.USER_MESSAGE_ACTIVITE_PASS);
            messageReleaseVo.setMobile(mobile);
            //update 3.0 长链接转为短链接
            String longUrl =url + aesTokenSms;
            String shortUrl = messageManageService.sendLinkMessageCommon(longUrl);
            log.info("Interface generation shortUrl{}",shortUrl);
            shortUrl= shortUrl !=null ? shortUrl :longUrl;
            messageReleaseVo.setSmsParams(new String[]{contact,shortUrl});
        }
        if (!StringUtils.isEmpty(email)) {
            String aesTokenEmail = AESCrypt.encrypt(user.getId() + "_" + receiverId + "_EMAIL_" + System.currentTimeMillis(), MessageConstants.USER_MESSAGE_ACTIVITE_PASS);
            messageReleaseVo.setEmail(email);
            messageReleaseVo.setEmailParams(new String[]{email, contact, url + aesTokenEmail, url + aesTokenEmail});
        }
        return messageManageService.abnormalMessageRelease(messageReleaseVo, true);
    }

    /**
     * desc:当前用户的联系信息,优先获取邮箱,如果邮箱为空则获取手机号
     *
     * @author chenxinyi
     * @date 2018/11/14 11:43
     */
    public String getContactInfo() {
        User user = SecurityContextUtil.getLoginUser();
        String contact = user.getEmail();
        if (StringUtils.isEmpty(contact)) {
            log.info("调用用户中心接口获取用户详情，userid：{}，time：{}", user.getId(), new Date());
            UserInfoBean userInfoBean = messageManageService.getUserInfoById(user.getId());
            if (null != userInfoBean) {
                contact = userInfoBean.getMobile();
            } else {
                log.error("获取用户联系信息失败，userid：{}", user.getId());
                throw new ApiException("1", "获取用户联系信息失败");
            }
        }
        return contact;
    }


    /**
     * 全部消息分页
     *
     * @param page            Page
     * @param messageMainType String
     * @return List<OperationMessageEntity>
     */

    public List<OperationMessageEntity> queryMessageForPage(Page page, String messageMainType, Boolean isUnread) {
        User user = SecurityContextUtil.getLoginUser();
        mergeMessageList(user);
        Map queryMap = new HashMap();
        queryMap.put("page", page);
        queryMap.put("userId", user.getId());
        queryMap.put("isUnread", isUnread);
        if (MessageConstants.MESSAGE_GET_OTHER.equals(messageMainType)) {
            queryMap.put("isOther", true);
            queryMap.put("messageMainType", "");
        } else {
            queryMap.put("isOther", false);
            queryMap.put("messageMainType", messageMainType);
        }
        return userMessageMapper.queryAllMessageForPage(queryMap);
    }

    /**
     * operation_user_message 添加 operation_message_list中的多余数据 方法1：
     */
    private void mergeMessageList(User user) {
        List<OperationMessageList> messageLists = operationMessageListMapper.getOperationMessageList();
        List<OperationMessageEntity> messageEntities = operationMessageMapper.getList(null, null, user.getId(), MessageTemplateTypeEnum.MAIL.getType());
        List<OperationMessageEntity> undueMessage = new ArrayList<>();
        if (messageLists != null && messageLists.size() > 0) {
            if (messageEntities != null && messageEntities.size() > 0) {
                //因目前数据库数据与该条件矛盾因此注释掉
                /*  if(messageLists.size() > messageEntities.size()){*/
                Map<BigInteger, OperationMessageEntity> messageEntityMap = listToMap(messageEntities);
                for (OperationMessageList messageList : messageLists) {
                    if (!messageEntityMap.containsKey(messageList.getId())) {
                        OperationMessageEntity messageEntity = getOperationEntity(user, messageList);
                        undueMessage.add(messageEntity);
                    }
                }
                /* }*/
            } else {
                for (OperationMessageList messageList : messageLists) {
                    OperationMessageEntity messageEntity = getOperationEntity(user, messageList);
                    undueMessage.add(messageEntity);
                }
            }
            log.info("从operation_message_list拉取数据到-->operation_user_messsage ,undueMessage的size {}    undueMessage {},current user id {} , userName {}",undueMessage.size(),undueMessage ,user.getId(),user.getName());
            //执行添加操作
            if (undueMessage.size() > 0) {
                //获取用户已经设置了的UserMessageSetting的数据
                List<UserMessageSettingEntity> list = messageManageMapper.getMessageSettingByMsgTypes(user.getId(), undueMessage);
                List<OperationMessageEntity> sendMessage = new ArrayList<>();
                if (list == null) {
                    Map<BigInteger, UserMessageSettingEntity> map = listToMaps(getDefaultMessageSetting(undueMessage));
                    for (OperationMessageEntity undueMessage1 : undueMessage) {
                        if (MAIL_SEND_SETTING_Y.equals(map.get(undueMessage1.getMessageType()).getSendMailSetting())) {
                            sendMessage.add(undueMessage1);
                        }
                    }
                } else {
                    Map<BigInteger, UserMessageSettingEntity> mapSetting = listToMaps(list);
                    for (OperationMessageEntity undueMessage1 : undueMessage) {
                        if (mapSetting.containsKey(undueMessage1.getMessageType())) {
                            if (MAIL_SEND_SETTING_Y.equals(mapSetting.get(undueMessage1.getMessageType()).getSendMailSetting())) {
                                log.info("add operation_user_message , undueMessage1{}",undueMessage1 );

                                sendMessage.add(undueMessage1);
                            }
                        } else {
                            UserMessageSettingEntity messageSettingEntity = messageManageService.sendMessageByDefaultSetting(undueMessage1.getMessageType(), user.getId());
                            if (MAIL_SEND_SETTING_Y.equals(messageSettingEntity.getSendMailSetting())) {
                                sendMessage.add(undueMessage1);
                            }
                        }
                    }
                }
                log.info("add operation_user_messsage ,sendMessage {}    sendMessage {}",sendMessage.size(),sendMessage );

                if (sendMessage.size() > 0) {
                    messageManageMapper.addMessage(sendMessage);
                }
            }
        }
    }

    private List<UserMessageSettingEntity> getDefaultMessageSetting(List<OperationMessageEntity> list) {
        List<MessageTypeBean> messageTypeBean = messageManageMapper.getMessageTypeByIds(list);
        List<UserMessageSettingEntity> messageSettingEntities = new ArrayList<>();
        for (MessageTypeBean messageTypeBean1 : messageTypeBean) {
            UserMessageSettingEntity messageSettingEntity = new UserMessageSettingEntity();
            messageSettingEntity.setSendEmailSetting(messageTypeBean1.getSendEmailDefaultSetting());
            messageSettingEntity.setSendMailSetting(messageTypeBean1.getSendMailDefaultSetting());
            messageSettingEntity.setSendMobileSetting(messageTypeBean1.getSendMobileDefaultSetting());
            messageSettingEntity.setMainTypeId(messageTypeBean1.getParentId());
            messageSettingEntity.setMainTypeName(messageTypeBean1.getParentName());
            messageSettingEntity.setMessageTypeId(messageTypeBean1.getId());
            messageSettingEntity.setMessageTypeName(messageTypeBean1.getTypeName());
            messageSettingEntities.add(messageSettingEntity);
        }
        return messageSettingEntities;
    }

    private Map<BigInteger, UserMessageSettingEntity> listToMaps(List<UserMessageSettingEntity> list) {
        Map<BigInteger, UserMessageSettingEntity> map = new HashMap<>();
        for (UserMessageSettingEntity settingEntity : list) {
            map.put(settingEntity.getMessageTypeId(), settingEntity);
        }
        return map;
    }

    private Map<BigInteger, OperationMessageEntity> listToMap(List<OperationMessageEntity> messageEntities) {
        Map<BigInteger, OperationMessageEntity> map = new HashMap<>();
        for (OperationMessageEntity messageEntity : messageEntities) {
            map.put(messageEntity.getMessageId(), messageEntity);
        }
        return map;
    }

    private OperationMessageEntity getOperationEntity(User user, OperationMessageList messageList) {
        OperationMessageEntity messageEntity = new OperationMessageEntity();
        messageEntity.setId(BigInteger.valueOf(IdWorker.getNextId()));
        messageEntity.setContent(messageList.getMailContent());
        messageEntity.setMessageId(messageList.getId());
        messageEntity.setCreatedTime(messageList.getCreatedTime());
        messageEntity.setMessageMainName(messageList.getMessageMainName());
        messageEntity.setMessageMainType(messageList.getMessageMainType());
        messageEntity.setMessageTemplateType(MessageTemplateTypeEnum.MAIL.getType());
        messageEntity.setMessageTypeName(messageList.getMessageTypeName());
        messageEntity.setMessageType(messageList.getMessageType());
        messageEntity.setReceiverId(user.getId());
        messageEntity.setSendUserId(messageList.getCreatedUserId());
        messageEntity.setTitle(messageList.getMailTitle());
        messageEntity.setStatus(messageList.getStatus());
        messageEntity.setReceiverContact(user.getId());
        return messageEntity;
    }

    /**
     * desc: Recipient management list
     *
     * @return responseBean  result Recipient management list
     * @author renhaixiang
     * @date 2018/9/29 16:20
     **/
    public ResponseBean getReceiverListByUserId(String id) {
        ResponseBean responseBean = new ResponseBean();
        UserReceiverListVo userReceiverListVo = new UserReceiverListVo();
        List<UserMessageReceiverInfoEntity> userMessageReceiverList = userMessageMapper.getReceiversByCreateUserId(id);
        if (userMessageReceiverList == null) {
            userMessageReceiverList = new ArrayList<>();
        }
        //获取当前用户基本信息
        log.info("获取用户[userid:{}]基本信息", id);
        UserInfoBean userInfoBean = messageManageService.getUserInfoById(id);
        UserMessageReceiverInfoEntity userMessageReceiverInfoEntity = new UserMessageReceiverInfoEntity();
        userMessageReceiverInfoEntity.setId(BigInteger.valueOf(IdWorker.getNextId()));
        userMessageReceiverInfoEntity.setReceiverName(MessageConstants.RECEIVER_SELF);
        userMessageReceiverInfoEntity.setReceiverEmail(userInfoBean.getEmail());
        userMessageReceiverInfoEntity.setReceiverMobile(userInfoBean.getMobile());
        userMessageReceiverInfoEntity.setIsUserSelf(MessageConstants.STATUS_YES);
        UserMessageReceiverEntity userMessageReceiverEntity = new UserMessageReceiverEntity();
        userMessageReceiverEntity.setId(userMessageReceiverInfoEntity.getId());
        Date date = TimeUtil.string2DateTime("2018-01-01 00:00:01");
        userMessageReceiverEntity.setCreatedTime(date);
        userMessageReceiverEntity.setCreatedUserName(userInfoBean.getUserName());
        userMessageReceiverEntity.setCreatedUserId(id);
        userMessageReceiverEntity.setReceiverName(MessageConstants.RECEIVER_SELF);
        userMessageReceiverEntity.setReceiverEmail(userInfoBean.getEmail());
        userMessageReceiverEntity.setReceiverMobile(userInfoBean.getMobile());
        userMessageReceiverEntity.setEmailCheckPass(MessageConstants.STATUS_YES);
        userMessageReceiverEntity.setMobileCheckPass(MessageConstants.STATUS_YES);
        userMessageReceiverEntity.setStatus(MessageConstants.STATUS_YES);
        userMessageReceiverEntity.setIsUserSelf(MessageConstants.STATUS_YES);
        Integer count = userMessageMapper.getMessageReceiverSelf(id);
        //判断消息接收人列表是否有用户本人
        if(count==0){
            int success = userMessageMapper.addMessageReceiverSelf(userMessageReceiverEntity);
            userMessageReceiverList.add(0, userMessageReceiverInfoEntity);
        }
        userReceiverListVo.setData(userMessageReceiverList);
        if (userMessageReceiverList.size() >= MessageConstants.RECEIVER_MAX_NUM) {
            userReceiverListVo.setReceiverMax(true);
        } else {
            userReceiverListVo.setReceiverMax(false);
        }
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setMessage(MessageConstants.RECEIVER_LIST_SUCCESS);
        responseBean.setResult(userReceiverListVo);
        return responseBean;
    }

    /**
     * 通过联系人姓名查询接收人
     * @param receiverName
     * @return
     */
    public ResponseBean getReceiverListByName(String receiverName) {
        String userId = SecurityContextUtil.getLoginUser().getId();
        ResponseBean responseBean = new ResponseBean();
        List<UserMessageReceiverInfoEntity> userMessageReceiverList = userMessageMapper.getReceiversByName(userId,receiverName);
        if(userMessageReceiverList.size()>0){
            responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
            responseBean.setMessage(MessageConstants.RECEIVER_LIST_SUCCESS);
        }else{
            responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
            responseBean.setMessage("消息接收人不存在");
        }
        responseBean.setResult(userMessageReceiverList);
        return responseBean;

    }

    /**
     * description: 接收人管理  判断参数正确性
     *
     * @param userMessageReceiverEntity 接收人前台传递参数
     * @return responseBean
     * @author renhaixiang
     * @date 2018/9/29 19:38
     **/
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean addMessageReceiver(String[] messageTypeIds, UserMessageAddVo userMessageReceiverEntity) {
        ResponseBean responseBean = new ResponseBean();
        User user = SecurityContextUtil.getLoginUser();
        log.info("添加消息接收人userId:{}", user.getId());
        if (userMessageMapper.getReceiverCount(user.getId()) > MessageConstants.RECEIVER_MAX_NUM) {
            log.info("当前账户下消息接收人数量:{}", userMessageMapper.getReceiverCount(user.getId()));
            responseBean.setCode("1");
            responseBean.setMessage(MessageConstants.RECEIVER_OVERFLOW_INFO);
            return responseBean;
        } else {
            //设置主键
            userMessageReceiverEntity.setId(BigInteger.valueOf(IdWorker.getNextId()));
            userMessageReceiverEntity.setCreatedTime(new Date());
            userMessageReceiverEntity.setCreatedUserId(user.getId());
            userMessageReceiverEntity.setCreatedUserName(user.getName());
            userMessageReceiverEntity.setEmailCheckPass(MessageConstants.STATUS_NO);
            userMessageReceiverEntity.setMobileCheckPass(MessageConstants.STATUS_NO);
            userMessageReceiverEntity.setStatus(MessageConstants.STATUS_YES);
            userMessageReceiverEntity.setIsUserSelf(MessageConstants.STATUS_NO);

            //插入接收人
            int result = userMessageMapper.addMessageReceiverM(userMessageReceiverEntity);
            // 判断消息类型id数组是否为空 如果不为空 根据messageTypeIds获取 user_message_setting 表中的id
            if (messageTypeIds != null && messageTypeIds.length > 0) {
                /**
                 *    select id from user_message_setting where main_type_id in
                 *         <foreach collection="arr" item="ids" separator="," open="(" close=")" >
                 *             #{ids}
                 *         </foreach>
                 *         and created_user_id = #{userId}
                 */
                List<BigInteger> listIds = userMessageMapper.getUserMeaasegeSettingIdByMainId(messageTypeIds, user.getId());
                List<UserMessageReceiverSettingEntity> listReceivers = new ArrayList<>();
                //如果listIds不为空向user_message_receiver_setting表中插入数据
                if (listIds != null && listIds.size() > 0) {
                    for (BigInteger id : listIds) {
                        UserMessageReceiverSettingEntity userMessageReceiverSetting = new UserMessageReceiverSettingEntity();
                        userMessageReceiverSetting.setCreatedTime(new Date());
                        userMessageReceiverSetting.setCreatedUserId(user.getId());
                        userMessageReceiverSetting.setStatus(MessageConstants.STATUS_YES);
                        userMessageReceiverSetting.setId(BigInteger.valueOf(IdWorker.getNextId()));
                        userMessageReceiverSetting.setMessageSettingId(id);
                        userMessageReceiverSetting.setReceiverId(userMessageReceiverEntity.getId());
                        listReceivers.add(userMessageReceiverSetting);
                    }
                }
                userMessageMapper.addMessageReceiverSetting(listReceivers);
            }
            return receiverInvite(userMessageReceiverEntity.getId().toString(), userMessageReceiverEntity.getReceiverMobile(), userMessageReceiverEntity.getReceiverEmail());
        }
    }

    /**
     * 修改接收人
     *
     * @param userMessageReceiverEntity UserMessageReceiverEntity
     * @return ResponseBean
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean updateMessageReceiver(UserMessageReceiverEntity userMessageReceiverEntity) {
        ResponseBean responseBean = new ResponseBean();
        User user = SecurityContextUtil.getLoginUser();
        UserMessageReceiverEntity userMagReceiver = userMessageMapper.getReceiverEmialAndMobileById(userMessageReceiverEntity.getId().toString(), user.getId());
        // email与修改之前的进行比较
        if (!userMagReceiver.getReceiverEmail().equals(userMessageReceiverEntity.getReceiverEmail())) {
            //如果与之前的不相同则将EmailCheckPass设置为N
            userMessageReceiverEntity.setEmailCheckPass(MessageConstants.STATUS_NO);
        }
        // mobile与修改之前的进行比较
        //如果与之前的不相同则将MobileCheckPass设置为N
        if (!userMagReceiver.getReceiverMobile().equals(userMessageReceiverEntity.getReceiverMobile())) {
            userMessageReceiverEntity.setMobileCheckPass(MessageConstants.STATUS_NO);
        }
        userMessageReceiverEntity.setCreatedUserId(user.getId());
        userMessageReceiverEntity.setUpdatedTime(new Date());
        userMessageMapper.updateMessageReceiver(userMessageReceiverEntity);
        //如果mobile与email都为空则不发送验证返回修改结果
        if (userMagReceiver.getReceiverMobile().equals(userMessageReceiverEntity.getReceiverMobile())
                && userMagReceiver.getReceiverEmail().equals(userMessageReceiverEntity.getReceiverEmail())) {
            responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
            responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
            return responseBean;
        }
        if (userMagReceiver.getReceiverEmail().equals(userMessageReceiverEntity.getReceiverEmail())) {
            userMessageReceiverEntity.setReceiverEmail("");
        }
        if (userMagReceiver.getReceiverMobile().equals(userMessageReceiverEntity.getReceiverMobile())) {
            userMessageReceiverEntity.setReceiverMobile("");
        }
        return receiverInvite(userMessageReceiverEntity.getId().toString(), userMessageReceiverEntity.getReceiverMobile(), userMessageReceiverEntity.getReceiverEmail());
    }

    /**
     * desc: Delete receiver based on id
     *
     * @return ResponseBean
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean removeMessageReceiver(String id) {
        User user = SecurityContextUtil.getLoginUser();
        ResponseBean responseBean = new ResponseBean();
        //判断该联系人是否为某类型消息唯一接收人
        List<UserMessageSettingEntity> messageSettingEntities = userMessageMapper.getCurrentReceiverMessageSetting(user.getId(), id);
        if (messageSettingEntities != null && !messageSettingEntities.isEmpty()) {
            responseBean.setCode(ResponseCode.RESPONSE_CODE_FAILURE);
            int receiversNum = messageSettingEntities.size();
            String receiversNumDescribe = "";
            if (receiversNum > 1) {
                receiversNumDescribe = "等" + receiversNum + "个消息类型";
            }
            responseBean.setMessage(String.format(MessageConstants.RECEIVER_RELATED_MESSAGE_TYPE, messageSettingEntities.get(0).getMainTypeName() + "-" + messageSettingEntities.get(0).getMessageTypeName(), receiversNumDescribe));
            return responseBean;
        }
        //根据id获取联系人信息
        UserMessageReceiverEntity userMessageReceiverEntity = userMessageMapper.getUserReceiver(new BigInteger(id));
        if(userMessageReceiverEntity !=null) {
            //逻辑删除接收人
            userMessageMapper.removeMessageReceiver(user.getId(), id);
            //逻辑删除接收人设置
            userMessageMapper.removeMessageReceiverSetting(user.getId(), id);

            MessageReleaseVo messageReleaseVo = new MessageReleaseVo();
            messageReleaseVo.setTemplateId(MessageConstants.COMMON_CONFIG_RECEIVER_DELETE_TEMPLATE_ID);
            messageReleaseVo.setEmail(userMessageReceiverEntity.getReceiverEmail());
            messageReleaseVo.setMobile(userMessageReceiverEntity.getReceiverMobile());
            String[] params = new String[]{user.getName()};
            messageReleaseVo.setSmsParams(params);
            messageReleaseVo.setEmailParams(params);
            //状态变更发送通知信息。
            if (!(StringUtils.isEmpty(messageReleaseVo.getReceiverId()) && StringUtils.isEmpty(messageReleaseVo.getEmail()) && StringUtils.isEmpty(messageReleaseVo.getMobile()))) {
                responseBean = messageRelease.messageRelease(messageReleaseVo);
            }

        }
        responseBean.setMessage(MessageConstants.MESSAGE_RECEIVER_REMOVE_SUCCESS);
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        return responseBean;
    }

    /**
     * 返回前端，接收人信息校验结果
     *
     * @param userMessageReceiverEntity
     */
    public ResponseBean messageCode(UserMessageReceiverEntity userMessageReceiverEntity) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode("0");
        if (!StringUtils.isEmpty(userMessageReceiverEntity.getReceiverEmail()) && !CommonUtils.checkEmail(userMessageReceiverEntity.getReceiverEmail())) {
            responseBean.setCode("1");
            responseBean.setMessage(MessageConstants.EMAIL_FORMAT_NOT_CORRECT);
            return responseBean;
        }
        if (!StringUtils.isEmpty(userMessageReceiverEntity.getReceiverMobile()) && !CommonUtils.checkMobile(userMessageReceiverEntity.getReceiverMobile())) {
            responseBean.setCode("1");
            responseBean.setMessage(MessageConstants.MOBILE_FORMAT_NOT_CORRECT);
            return responseBean;
        }
        return responseBean;
    }

    /**
     * 删除消息
     *
     * @param ids String[]
     * @return ResponseBean
     */
    @Transactional
    public ResponseBean deleteMessage(String[] ids) {
        // 声明返回对象
        ResponseBean responseBean = new ResponseBean();
        if (null == ids || 0 == ids.length) {
            responseBean.setMessage(MessageConstants.DELETE_MESSAGE_SUCCESS);
            responseBean.setCode(MessageConstants.DELETE_MESSAGE_SUCCESS_CODE);
            return responseBean;
        }
        // 获取当前登录用户
        User user = SecurityContextUtil.getLoginUser();
        log.info("删除消息 userId:{},删除消息ids:[{}]", user.getId(), ids);
        MessageDeleteDTO messageDeleteDTO = new MessageDeleteDTO();
        // 获取当前时间
        Date date = new Date();
        // 格式化时间
        messageDeleteDTO.setDeleteFlag(TimeUtil.dateTime2String(new Date()));
        messageDeleteDTO.setUpdatedUserId(user.getId());
        messageDeleteDTO.setUpdatedTime(date);
        int result = userMessageMapper.deleteMessage(ids, messageDeleteDTO);
        if (result != 0) {
            responseBean.setMessage(MessageConstants.DELETE_MESSAGE_SUCCESS + ":" + result + "条");
            responseBean.setCode(MessageConstants.DELETE_MESSAGE_SUCCESS_CODE);
        } else {
            responseBean.setMessage(MessageConstants.DELETE_MESSAGE_FAIL);
            responseBean.setCode(MessageConstants.DELETE_MESSAGE_FAIL_CODE);
        }
        return responseBean;
    }


    /**
     * 标记为已读信息
     *
     * @param markUnreadMessageVo MarkUnreadMessageVo
     * @return ResponseBean
     */
    @Transactional
    public ResponseBean markAsRead(MarkUnreadMessageVo markUnreadMessageVo) {
        // 获取当前登录用户
        User user = SecurityContextUtil.getLoginUser();
        log.info("标记信息为已读userId:{},标记已读的信息ids:[{}]", user.getId(), markUnreadMessageVo.getIds());
        // 声明返回对象
        ResponseBean responseBean = new ResponseBean();
        String[] ids = markUnreadMessageVo.getIds();
        if (0 == ids.length) {
            responseBean.setCode(MessageConstants.MARK_AS_READ_SUCCESS_CODE);
            responseBean.setMessage(MessageConstants.MARK_AS_READ_SUCCESS);
            return responseBean;
        }
        if (ids.length == 1) {
            if (!StringUtils.isEmpty(ids[0]) && MARK_ALL_READ.equals(ids[0])) {
                ids = userMessageMapper.getMessageIdsByReceiverId(user.getId());
            }
        }
        MessageDeleteDTO messageDeleteDTO = new MessageDeleteDTO();
        Date date = new Date();
        messageDeleteDTO.setUpdatedTime(date);
        // 标记为已读
        messageDeleteDTO.setReadStatus(MessageConstants.STATUS_YES);
        messageDeleteDTO.setUpdatedUserId(user.getId());
        messageDeleteDTO.setReadTime(TimeUtil.dateTime2String(new Date()));
        int result = userMessageMapper.deleteMessage(ids, messageDeleteDTO);
        if (result != 0) {
            responseBean.setMessage(MessageConstants.MARK_AS_READ_SUCCESS);
            responseBean.setCode(MessageConstants.MARK_AS_READ_SUCCESS_CODE);
        } else {
            responseBean.setCode(MessageConstants.MARK_AS_READ_FAIL_CODE);
            responseBean.setMessage(MessageConstants.MARK_AS_READ_FAIL);
        }
        return responseBean;
    }

    /**
     * 批量添加消息接收人
     *
     * @param settingIds   String[]
     * @param receiversIds String[]
     * @return ResponseBean
     */
    @Transactional
    public ResponseBean receiverBatchAdd(String[] settingIds, String[] receiversIds) {
        ResponseBean responseBean = new ResponseBean();
        if (null == receiversIds || receiversIds.length == 0) {
            responseBean.setMessage(MessageConstants.BATCH_RECEIVER_ADD_FAIL);
            responseBean.setCode(MessageConstants.BATCH_RECEIVER_ADD_FAIL_CODE);
            return responseBean;
        }
        if (settingIds == null || settingIds.length == 0) {
            responseBean.setMessage(MessageConstants.BATCH_RECEIVER_ADD_FAIL);
            responseBean.setCode(MessageConstants.BATCH_RECEIVER_ADD_FAIL_CODE);
            return responseBean;
        }
        Date createdDate = new Date();
        User user = SecurityContextUtil.getLoginUser();
        HashSet<String> receiverIdSet=new HashSet();
        HashSet<String> settingIdSet=new HashSet();
        for(String receiverId : receiversIds){
            receiverIdSet.add(receiverId);
        }
        for(String settingId: settingIds){
            settingIdSet.add(settingId);
        }
        log.info("批量添加消息接收人 userId:{},settingIds:[{}],receiversIds:[{}]", user.getId(), settingIds, receiversIds);
        // 循环要添加的消息接收人
        Iterator<String> it1 = receiverIdSet.iterator();
        while (it1.hasNext()) {
            String receiverId = it1.next();
            List<UserMessageReceiverSettingEntity> addList = new ArrayList<>();
            List<UserMessageReceiverSettingEntity> updateList = new ArrayList<>();
            Iterator<String> it2 = settingIdSet.iterator();
            // 循环消息设置id
            while (it2.hasNext()) {
                String settingId = it2.next();
                String isHas = userMessageMapper.isHasReceiver(user.getId(), settingId, receiverId);
                if (StringUtils.isEmpty(isHas)) {
                    // 如果isHas是空则执行添加
                    UserMessageReceiverSettingEntity userMessageReceiverSetting = new UserMessageReceiverSettingEntity();
                    userMessageReceiverSetting.setReceiverId(BigInteger.valueOf(Long.parseLong(receiverId)));
                    userMessageReceiverSetting.setMessageSettingId(BigInteger.valueOf(Long.parseLong(settingId)));
                    userMessageReceiverSetting.setId(BigInteger.valueOf(IdWorker.getNextId()));
                    userMessageReceiverSetting.setStatus(MessageConstants.STATUS_YES);
                    userMessageReceiverSetting.setCreatedUserId(user.getId());
                    userMessageReceiverSetting.setCreatedTime(createdDate);
                    addList.add(userMessageReceiverSetting);
                } else if (MessageConstants.STATUS_NO.equals(isHas)) {
                    UserMessageReceiverSettingEntity userMessageReceiverSettingUp = new UserMessageReceiverSettingEntity();
                    userMessageReceiverSettingUp.setCreatedUserId(user.getId());
                    userMessageReceiverSettingUp.setUpdatedUserId(user.getId());
                    userMessageReceiverSettingUp.setStatus(MessageConstants.STATUS_YES);
                    userMessageReceiverSettingUp.setReceiverId(BigInteger.valueOf(Long.parseLong(receiverId)));
                    userMessageReceiverSettingUp.setMessageSettingId(BigInteger.valueOf(Long.parseLong(settingId)));
                    userMessageReceiverSettingUp.setUpdatedTime(createdDate);
                    updateList.add(userMessageReceiverSettingUp);
                }
            }
            if (addList.size() > 0) {
                userMessageMapper.addMessageReceiverSetting(addList);
            }
            if (updateList.size() > 0) {
                userMessageMapper.updateMessageReceiverSetting(updateList);
            }

        }
        responseBean.setMessage(MessageConstants.BATCH_RECEIVER_ADD_SUCCESS);
        responseBean.setCode(MessageConstants.BATCH_RECEIVER_ADD_SUCCESS_CODE);
        return responseBean;
    }


    /**
     * 批量删除操作
     *
     * @param settingIds   String[]
     * @param receiversIds String[]
     * @return ResponseBean
     */
    @Transactional
    public ResponseBean batchDeleteReceiver(String[] settingIds, String[] receiversIds) {
        ResponseBean responseBean = new ResponseBean();
        if (null == receiversIds || receiversIds.length == 0) {
            responseBean.setCode(MessageConstants.BATCH_RECEIVER_DELETE_FAIL_CODE);
            responseBean.setMessage(MessageConstants.BATCH_RECEIVER_DELETE_FAIL);
            return responseBean;
        }
        if (null == settingIds || settingIds.length == 0) {
            responseBean.setCode(MessageConstants.BATCH_RECEIVER_DELETE_FAIL_CODE);
            responseBean.setMessage(MessageConstants.BATCH_RECEIVER_DELETE_FAIL);
            return responseBean;
        }
        User user = SecurityContextUtil.getLoginUser();
        log.info("批量删除消息接收人;userId:{},settingIds:[{}],receiversIds:[{}]", user.getId(), settingIds, receiversIds);
        for (String receiverId : receiversIds) {
            for (String settingId : settingIds) {
                List<UserMessageReceiverSettingEntity> updateList = new ArrayList<>();
                UserMessageReceiverSettingEntity userMessRecSetting = new UserMessageReceiverSettingEntity();
                userMessRecSetting.setCreatedUserId(user.getId());
                userMessRecSetting.setMessageSettingId(new BigInteger(settingId));
                List<UserMessageReceiverSettingEntity> list = userMessageMapper.getUserMessageReceiverSettingEntity(userMessRecSetting);
                if (null == list || list.size() <= 1) {
                    continue;
                }
                // 获取接收人当前状态
                String isHas = userMessageMapper.isHasReceiver(user.getId(), settingId, receiverId);
                if (!StringUtils.isEmpty(isHas) && isHas.equals(MessageConstants.STATUS_YES)) {
                    UserMessageReceiverSettingEntity userMessageReceiverSettingEntity = new UserMessageReceiverSettingEntity();
                    userMessageReceiverSettingEntity.setStatus(MessageConstants.STATUS_NO);
                    userMessageReceiverSettingEntity.setUpdatedUserId(user.getId());
                    userMessageReceiverSettingEntity.setReceiverId(new BigInteger(receiverId));
                    userMessageReceiverSettingEntity.setCreatedUserId(user.getId());
                    userMessageReceiverSettingEntity.setMessageSettingId(BigInteger.valueOf(Long.parseLong(settingId)));
                    updateList.add(userMessageReceiverSettingEntity);
                    userMessageMapper.updateMessageReceiverSetting(updateList);
                }
            }
        }
        responseBean.setCode(MessageConstants.BATCH_RECEIVER_DELETE_SUCCESS_CODE);
        responseBean.setMessage(MessageConstants.BATCH_RECEIVER_DELETE_SUCCESS);
        return responseBean;
    }

    /**
     *
     * @return
     */
    public ResponseBean getMessageTypesCount(){
       User user = SecurityContextUtil.getLoginUser();
       ResponseBean responseBean = new ResponseBean();
       List<OperationMessageTypeEntity> operationMessageTypeEntityList = userMessageMapper.getMessageTypesCount();
       log.info("所有有效的父类型消息数量是：{}",operationMessageTypeEntityList.size());
       if(operationMessageTypeEntityList.size()!=0){
           Integer invalidCount = 0;
           Integer validCount = 0;
           List<MessageTypeCountVo> messageTypeCountVoList = new ArrayList<>();
           for(OperationMessageTypeEntity operationMessageTypeEntity:operationMessageTypeEntityList){
               if(StringUtils.equals(operationMessageTypeEntity.getStatus(),MessageConstants.STATUS_YES)){
                   List<BigInteger> childMessageTypeIds = userMessageMapper.getChildMessageTypeId(operationMessageTypeEntity.getId());
                   Integer customerCount = 0;
                   for(BigInteger childMessageTypeId:childMessageTypeIds){
                       Integer messageCount = userMessageMapper.getMessageCount(childMessageTypeId,user.getId());
                       //log.info("该messageType:{}消息类型有效的消息数量:{}",childMessageTypeId,messageCount);
                       customerCount+=messageCount;
                   }
                   validCount+=customerCount;
                   //父类型有效子类型无效
                   List<BigInteger> invalidChildIds = userMessageMapper.getInvalidChildIds(operationMessageTypeEntity.getId());
                   for(BigInteger childId : invalidChildIds){
                       Integer messageCount = userMessageMapper.getMessageCount(childId,user.getId());
                       invalidCount+=messageCount;
                   }
                   MessageTypeCountVo messageTypeCountVo = new MessageTypeCountVo();
                   messageTypeCountVo.setMainTypeName(operationMessageTypeEntity.getTypeName()+"("+customerCount+")");
                   messageTypeCountVo.setMainTypeId(operationMessageTypeEntity.getId().toString());
                   messageTypeCountVoList.add(messageTypeCountVo);
               }else{
                   //将无效的消息加到其他类型中
                   List<BigInteger> childMessageTypeIds = userMessageMapper.getInvalidChildMessageTypeId(operationMessageTypeEntity.getId());
                   int otherCount = 0;
                   for(BigInteger childMessageTypeId:childMessageTypeIds){
                       Integer messageCount = userMessageMapper.getMessageCount(childMessageTypeId,user.getId());
                       otherCount+=messageCount;
                   }
                   invalidCount +=otherCount;
               }
           }
           MessageTypeCountVo messageTypeCountVo = new MessageTypeCountVo();
           log.info("全部无效的消息类型数量:{}",invalidCount);
           messageTypeCountVo.setMainTypeName(MessageConstants.MESSAGE_TYPE_OTHER+"("+invalidCount+")");
           messageTypeCountVo.setMainTypeId(MessageConstants.MESSAGE_GET_OTHER);
           messageTypeCountVoList.add(messageTypeCountVo);
           log.info("全部有效的消息类型数量:{}",validCount);
           MessageTypeCountVo messageTypeValidCountVo = new MessageTypeCountVo();
           messageTypeValidCountVo.setMainTypeName(MessageConstants.MESSAGE_TYPE_ALL+"("+ validCount+")");
           messageTypeValidCountVo.setMainTypeId(MessageConstants.MESSAGE_GET_ALL);
           messageTypeCountVoList.add(MessageConstants.MESSAGE_NUM_ZERO,messageTypeValidCountVo);
           responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
           responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
           responseBean.setResult(messageTypeCountVoList);
       }else{
           responseBean.setCode(ResponseCode.RESPONSE_CODE_FAILURE);
           responseBean.setMessage(MessageConstants.NONE_VALID_EXISTENCE_MESSAGE_TYPE);
       }
       return responseBean;
    }

    /**
     *
     * @param userId
     * @return
     */
    public ResponseBean getUserAllMessageSetting(String userId){
        ResponseBean responseBean = new ResponseBean();
        List<UserMessageSettingEntity> userSettingList = userMessageMapper.getUserMessageSetting(userId);
        List<OperationMessageTypeEntity> defaultList = userMessageMapper.getDefaultMessageSetting();
        List<UserMessageSettingEntity> addUserSettingList = new ArrayList<>();
        List<UserMessageReceiverSettingEntity> umrsList = new ArrayList<>();
        List<UserMessageReceiverSettingEntity> addUmrsList = new ArrayList<>();
        List<String> isUserSelfs = userMessageMapper.getUserSelfNumber(userId);
        BigInteger receiverId=addUserMessageReceiver(userId).getId();
        if(!(isUserSelfs.size()>0)){
            //将自己作为接收人更新到接收人列表
            userMessageMapper.addMessageReceiverM(addUserMessageReceiver(userId));
            log.info("增加的账号联系人id是：{}",receiverId);
        }
        if (0 == userSettingList.size()) {
            //添加默认配置
            addDefaultSetting(defaultList, userId, userSettingList);
            for (UserMessageSettingEntity userMes : userSettingList) {
                //将自己作接收人更新到关联表
                umrsList.add(assembleUserMessReceiverSettingEntity(userMes.getId(), userId,receiverId));
            }
            userMessageMapper.addMessageReceiverSetting(umrsList);
        } else {
            //只判断增加的情况
            boolean flag;
            for (OperationMessageTypeEntity omte : defaultList) {
                flag = false;
                for (UserMessageSettingEntity userMes : userSettingList) {
                    if (0 == omte.getId().compareTo(userMes.getMessageTypeId())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    UserMessageSettingEntity userMessageSettingEntity = getUserMessSetByOperMessType(userId, omte);
                    addUserSettingList.add(userMessageSettingEntity);
                    addUmrsList.add(assembleUserMessReceiverSettingEntity(userMessageSettingEntity.getId(), userId,receiverId));
                }
            }
            if (!addUserSettingList.isEmpty()) {
                userSettingList.addAll(addUserSettingList);
                userMessageMapper.addUserMessageSetting(addUserSettingList);
                userMessageMapper.addMessageReceiverSetting(addUmrsList);
            }
        }
        List<UserMessageSettingVo> userMessageSettingVoList = new ArrayList<>();
        List<BigInteger> mainTypeIds = userMessageMapper.getUserAllMainTypeId(userId);
        if(mainTypeIds.size()==0){
            responseBean.setCode(ResponseCode.RESPONSE_CODE_FAILURE);
            responseBean.setMessage(MessageConstants.NONE_VALID_EXISTENCE_MESSAGE_TYPE);
        }else{
            for(BigInteger mainTypeId : mainTypeIds){
                List<UserMessageSettingEntity> userMessageSettingEntityList = userMessageMapper.getUserMessageSettingByMainId(mainTypeId,userId);
                UserMessageSettingVo userMessageSettingVo = new UserMessageSettingVo();
                int mailCount = 0;
                int emailCount = 0;
                int smsCount = 0;
                for(UserMessageSettingEntity userMessageSettingEntity : userMessageSettingEntityList){
                    if(StringUtils.equals(userMessageSettingEntity.getSendMailSetting(),MessageConstants.STATUS_YES)){
                        mailCount++;
                    }
                    if(StringUtils.equals(userMessageSettingEntity.getSendEmailSetting(),MessageConstants.STATUS_YES)){
                        emailCount++;
                    }
                    if(StringUtils.equals(userMessageSettingEntity.getSendMobileSetting(),MessageConstants.STATUS_YES)){
                        smsCount++;
                    }
                    userMessageSettingVo.setMessageTypeName(userMessageSettingEntity.getMainTypeName());
                    //返回值增加接收人id和接收人姓名列表
                    List<UserMessageReceiverInfoEntity> userMessageReceiverInfoEntityList = new ArrayList<>();
                    //添加除0以外的接收人
                    List<UserMessageReceiverInfoEntity> userMessageReceiverInfoEntities = userMessageMapper.getMessageReceiverInfoById(userMessageSettingEntity.getId()+"", userId);
                    userMessageReceiverInfoEntities.forEach(userMessageReceiverInfoEntity -> {
                        if(!StringUtils.equals("0",userMessageReceiverInfoEntity.getId()+"")){
                            userMessageReceiverInfoEntityList.add(userMessageReceiverInfoEntity);
                        }
                    });
                    if(userMessageReceiverInfoEntityList.size()==0){
                        //默认至少添加账号联系人
                        userMessageReceiverInfoEntityList.addAll(userMessageMapper.getMessageReceiverSelfById(userId));
                    }
                    userMessageSettingEntity.setData(userMessageReceiverInfoEntityList);
                    log.info("此时的接收人信息：{},消息设置集合:{}",userMessageSettingEntity,userMessageSettingEntityList);
                }
                userMessageSettingVo.setSendMailSetting(chooseSendMsgSetting(mailCount,userMessageSettingEntityList));
                userMessageSettingVo.setSendEmailSetting(chooseSendMsgSetting(emailCount,userMessageSettingEntityList));
                userMessageSettingVo.setSendMobileSetting(chooseSendMsgSetting(smsCount,userMessageSettingEntityList));
                userMessageSettingVo.setMessageTypeId(mainTypeId);
                userMessageSettingVo.setChildren(userMessageSettingEntityList);
                userMessageSettingVoList.add(userMessageSettingVo);
            }
            responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
            responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
            responseBean.setResult(userMessageSettingVoList);
        }
        return responseBean;
    }

    /**
     *
     * @param count
     * @param list
     * @return
     */

    private String chooseSendMsgSetting(int count,List<UserMessageSettingEntity> list){
        String setting = "";
        if(count==list.size()){ setting=MessageConstants.MESSAGE_SETTING_ALL;}
        if(count==0) {setting=MessageConstants.MESSAGE_SETTING_NONE;}
        if(count>0&&count<list.size()) {setting=MessageConstants.MESSAGE_SETTING_SOME;}
        return setting;
    }

    /**
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean resetUserMsgSetting(){
        User user = SecurityContextUtil.getLoginUser();
        List<OperationMessageTypeEntity> operationMessageTypeEntityList = userMessageMapper.getDefaultMessageSetting();
        List<UserMessageSettingEntity> userMessageSettingList = userMessageMapper.getUserMessageSetting(user.getId());
        for (OperationMessageTypeEntity operationMessageTypeEntity : operationMessageTypeEntityList) {
            for (UserMessageSettingEntity userMessageSettingEntity : userMessageSettingList) {
                if (0 == operationMessageTypeEntity.getId().compareTo(userMessageSettingEntity.getMessageTypeId())) {
                    userMessageSettingEntity.setSendMobileSetting(operationMessageTypeEntity.getSendMobileDefaultSetting());
                    userMessageSettingEntity.setSendEmailSetting(operationMessageTypeEntity.getSendEmailDefaultSetting());
                    userMessageSettingEntity.setSendMailSetting(operationMessageTypeEntity.getSendMailDefaultSetting());
                    userMessageSettingEntity.setUpdatedTime(new Date());
                    userMessageSettingEntity.setCanEdit(operationMessageTypeEntity.getCanEdit());
                }
            }
        }
        userMessageMapper.editUserMessageSetting(userMessageSettingList);
        return getUserAllMessageSetting(user.getId());
    }

    /**
     * 查询不同主分类下消息数量
     * @param page
     * @param messageMainType
     * @param isUnread
     * @return
     */
    public List<OperationMessageEntity> queryValidMessageForPage(Page page, String messageMainType, Boolean isUnread) {
        User user = SecurityContextUtil.getLoginUser();
        mergeMessageList(user);
        Map queryMap = new HashMap();
        queryMap.put("page", page);
        queryMap.put("userId", user.getId());
        queryMap.put("isUnread", isUnread);
        if (MessageConstants.MESSAGE_GET_OTHER.equals(messageMainType)) {
            queryMap.put("isOther", true);
            return userMessageMapper.queryOtherMessageForPage(queryMap);
        } else {
            queryMap.put("isOther", false);
            queryMap.put("messageMainType", messageMainType);
            return userMessageMapper.queryAllMessageForPage(queryMap);
        }
    }
}
