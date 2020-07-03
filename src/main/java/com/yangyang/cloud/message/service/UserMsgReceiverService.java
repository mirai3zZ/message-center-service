package com.yangyang.cloud.message.service;

import com.inspur.bss.commonsdk.utils.IdWorker;
import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.ResponseCode;
import com.yangyang.cloud.common.bean.User;
import com.yangyang.cloud.common.exception.BusinessException;
import com.yangyang.cloud.common.exception.model.ErrorCodeEntity;
import com.yangyang.cloud.keycloak.SecurityContextUtil;
import com.yangyang.cloud.message.bean.*;
import com.yangyang.cloud.message.common.MessageConstants;
import com.yangyang.cloud.message.common.MessageErrorCode;
import com.yangyang.cloud.message.dto.MessageSettingAddDTO;
import com.yangyang.cloud.message.mapper.OperationMessageTypeMapper;
import com.yangyang.cloud.message.mapper.UserMessageMapper;
import com.yangyang.cloud.message.vo.UserMessageReceiverSettingVo;
import com.yangyang.cloud.message.vo.UserMsgReceiverSettingVo;
import com.yangyang.cloud.messagemanage.mapper.OperationMessageType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.*;

/**
 * description:
 *
 * @author: LiuYang01
 * @date: 2019/9/18 18:13
 */
@Service
@Slf4j
public class UserMsgReceiverService {

    @Autowired
    private UserMessageMapper userMessageMapper;
    @Autowired
    private OperationMessageTypeMapper operationMessageTypeMapper;
    @Autowired
    private MessageErrorCode messageErrorCode;

    /**
     * @param: receiverId
     * @return
     */
    public ResponseBean getReceiverContactById(String receiverId){
        User user = SecurityContextUtil.getLoginUser();
        log.info("user id [{}] name [{}] get receiverContact by Id", user.getId(), user.getName());
        ResponseBean responseBean = new ResponseBean();
        if(StringUtils.isNotBlank(receiverId)){
            UserMessageReceiverEntity userMessageReceiverEntity = userMessageMapper.getUserReceiver(new BigInteger(receiverId));
            Map receiverMap = new HashMap();
            if(ObjectUtils.hashCode(userMessageReceiverEntity)!=0){
                if(StringUtils.equals(userMessageReceiverEntity.getStatus(), MessageConstants.STATUS_YES)){
                    receiverMap.put("receiverName",userMessageReceiverEntity.getReceiverName());
                    receiverMap.put("receiverEmail",userMessageReceiverEntity.getReceiverEmail());
                    receiverMap.put("receiverMobile",userMessageReceiverEntity.getReceiverMobile());
                    receiverMap.put("receiverJob",userMessageReceiverEntity.getReceiverJob());
                    responseBean.setCode(MessageConstants.STRING_ZERO);
                    responseBean.setMessage(MessageConstants.GAIN_RECEIVER_CONTACT_SUCCESS);
                    responseBean.setResult(receiverMap);

                }else {
                    responseBean.setCode(MessageConstants.STRING_ONE);
                    responseBean.setMessage(MessageConstants.GAIN_RECEIVER_CONTACT_FAILURE);
                }
            }else{
                responseBean.setCode(MessageConstants.STRING_ONE);
                responseBean.setMessage(MessageConstants.NO_EXISTENCE_FOR_RECEIVER);

            }
        }else{
            ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.MESSAGE_RECEIVER_ID_NOT_EMPTY);
            responseBean.setCode(errorCodeEntity.getErrorCode());
            responseBean.setMessage(errorCodeEntity.getErrorMessage());
        }

        return responseBean;
    }

    /**
     *
     * @param messageType
     * @param userMessageReceiverSettingVo
     * @return
     */

    @Transactional(rollbackFor = Exception.class)
    public ResponseBean getMsgReceiverByType(String messageType,UserMessageReceiverSettingVo userMessageReceiverSettingVo) {
        ResponseBean responseBean = new ResponseBean();
        UserMessageReceiverSettingEntity[] arrays = userMessageReceiverSettingVo.getList();
        List<UserMessageReceiverSettingEntity> addList = new ArrayList<>();
        Collections.addAll(addList,arrays);
        List<UserMessageReceiverSettingEntity> userMessageReceiverSettingEntities = new ArrayList<>();
        List<UserMessageReceiverSettingEntity> userMsgAddEntities = new ArrayList<>();
        MessageSettingAddDTO messageSettingAddDTO = new MessageSettingAddDTO();
        List<UserMessageReceiverSettingEntity> failureList = new ArrayList<>();
        //判断接收人id是否存在
        for(UserMessageReceiverSettingEntity s:addList){
            Optional<BigInteger> receiverId = Optional.ofNullable(userMessageMapper.getMessageReceiverId(s.getId()));
            if(!receiverId.isPresent()){
                failureList.add(s);
            }
        }
        //接收人列表移除id不存在的接收人
        for(UserMessageReceiverSettingEntity failureId : failureList){
            addList.remove(failureId);
        }
        User user = SecurityContextUtil.getLoginUser();
        String createdUserId = user.getId();
        BigInteger messageTypeId = StringUtils.isNotBlank(messageType)?new BigInteger(messageType):userMessageReceiverSettingVo.getId();
        Map queryMap = new HashMap();
        queryMap.put("createdUserId",createdUserId);
        queryMap.put("messageTypeId",messageTypeId);
        //select id from user_message_setting where created_user_id = #{createdUserId} and message_type_id = #{messageTypeId}
        List<BigInteger> list = userMessageMapper.getMsgSettingId(queryMap);
        OperationMessageTypeEntity operationMessageTypeEntity = operationMessageTypeMapper.getOperationMessageTypeEntityById (messageTypeId);
        if (operationMessageTypeEntity == null) {
            log.info("消息类型不存在,messageTypeId:{}",messageTypeId);
            throw new BusinessException("消息类型不存在");
        }
        if(operationMessageTypeEntity.getTypeLevel()==0){
            List<OperationMessageTypeEntity> childMessageTypeEntities = operationMessageTypeMapper.getChildMessageByParentId(operationMessageTypeEntity.getId());
            log.info("根据父类型消息id查询出的子类型消息id数量:{}",childMessageTypeEntities!=null?childMessageTypeEntities.size():"子类型消息不存在！");
            for(OperationMessageTypeEntity operationMsgTypeEntity:childMessageTypeEntities){
                //判断addlist里是否有存在的消息接收人
                if(addList.size()!=0){
                    Map query = new HashMap();
                    query.put("createdUserId",createdUserId);
                    query.put("messageTypeId",operationMsgTypeEntity.getId());
                    List<BigInteger> messageTypeLists =  userMessageMapper.getMsgSettingId(queryMap);
                    messageSettingAddDTO = addOrUpdateToMsgSetting(messageTypeLists,addList,createdUserId,messageTypeId,operationMsgTypeEntity,userMsgAddEntities,userMessageReceiverSettingEntities);
                    log.info("关联表的更改信息，修改：{}",messageSettingAddDTO.getUserMsgAddEntities()==null?"messageUpdate"+messageSettingAddDTO.getUserMessageReceiverSettingEntities().size():"messageAdd"+messageSettingAddDTO.getUserMsgAddEntities().size());
                } else{
                    responseBean.setCode("1");
                    responseBean.setMessage("消息接收人不存在");
                    responseBean.setResult(failureList.toArray());
                    return responseBean;
                }
            }

        }else{
            if(addList.size()!=0){
                log.info("子类型消息id更新,接收人:{},operationMessageTypeEntity:{}",addList,operationMessageTypeEntity);
                messageSettingAddDTO = addOrUpdateToMsgSetting(list,addList,createdUserId,messageTypeId,operationMessageTypeEntity,userMsgAddEntities,userMessageReceiverSettingEntities);
            }else{
                responseBean.setCode("1");
                responseBean.setMessage("消息接收人不存在");
                responseBean.setResult(failureList.toArray());
                return responseBean;
            }
        }

        if(messageSettingAddDTO.getUserMsgAddEntities()!=null){
            userMessageMapper.addMessageReceiverSetting(messageSettingAddDTO.getUserMsgAddEntities());
            responseBean.setCode("0");
            responseBean.setMessage("更新成功");
            responseBean.setResult(addList.toArray());
            if(failureList.size()>0){
                responseBean.setMessage("部分消息接收人不存在，设置失败");
                responseBean.setResult(failureList.toArray());
            }
        }else {
            //批量更新接收人接受状态
            int result = userMessageMapper.updateMessageReceiverSetting(messageSettingAddDTO.getUserMessageReceiverSettingEntities());
            responseBean.setCode("0");
            responseBean.setMessage("更新成功");
            if(failureList.size()>0){
                responseBean.setMessage("部分消息接收人不存在，设置失败");
                responseBean.setResult(failureList.toArray());
            }

        }
        return responseBean;
    }

    /**
     *
     * @param list
     * @param addList
     * @param createdUserId
     * @param messageTypeId
     * @param operationMessageTypeEntity
     * @param userMsgAddEntities
     * @param userMessageReceiverSettingEntities
     * @return
     */

    public MessageSettingAddDTO addOrUpdateToMsgSetting(List<BigInteger> list,List<UserMessageReceiverSettingEntity> addList,String createdUserId,BigInteger messageTypeId,OperationMessageTypeEntity operationMessageTypeEntity, List<UserMessageReceiverSettingEntity> userMsgAddEntities,List<UserMessageReceiverSettingEntity> userMessageReceiverSettingEntities){
            MessageSettingAddDTO messageSettingAddDTO = new MessageSettingAddDTO();
            if(!CollectionUtils.isEmpty(list)){
                log.info("messageSettingId不存在时，{}",list.toArray());
                BigInteger messageSettingId = list.get(0);
                for(UserMessageReceiverSettingEntity s : addList){
                    Map map = new HashMap();
                    map.put("messageSettingId",messageSettingId);
                    map.put("receiverId",s.getId());
                    int count = userMessageMapper.getCountByMsgSettingId(map);
                    UserMessageReceiverSettingEntity userMsgReceiverSettingEntity = new UserMessageReceiverSettingEntity();
                    if(count < 1){
                        userMsgReceiverSettingEntity.setMessageSettingId(messageSettingId);
                        userMsgReceiverSettingEntity.setReceiverId(s.getId());
                        userMsgReceiverSettingEntity.setId(BigInteger.valueOf(IdWorker.getNextId()));
                        userMsgReceiverSettingEntity.setCreatedTime(new Date());
                        userMsgReceiverSettingEntity.setUpdatedTime(new Date());
                        userMsgReceiverSettingEntity.setCreatedUserId(createdUserId);
                        userMsgReceiverSettingEntity.setStatus(s.getStatus());
                        userMsgAddEntities.add(userMsgReceiverSettingEntity);
                        messageSettingAddDTO.setUserMsgAddEntities(userMsgAddEntities);
                    }else{
                        userMsgReceiverSettingEntity.setMessageSettingId(messageSettingId);
                        userMsgReceiverSettingEntity.setReceiverId(s.getId());
                        userMsgReceiverSettingEntity.setStatus(s.getStatus());
                        userMsgReceiverSettingEntity.setUpdatedTime(new Date());
                        userMessageReceiverSettingEntities.add(userMsgReceiverSettingEntity);
                        messageSettingAddDTO.setUserMessageReceiverSettingEntities(userMessageReceiverSettingEntities);
                    }
                }
            }else{
                //按照默认的新增加一条并更新联系人到user_message_setting表
                UserMessageSettingEntity userMessageSettingEntity = new UserMessageSettingEntity();
                userMessageSettingEntity.setId(BigInteger.valueOf(IdWorker.getNextId()));
                userMessageSettingEntity.setCreatedTime(new Date());
                userMessageSettingEntity.setCreatedUserId(createdUserId);
                userMessageSettingEntity.setMessageTypeId(messageTypeId);
                userMessageSettingEntity.setMessageTypeName(operationMessageTypeEntity.getTypeName());
                userMessageSettingEntity.setMainTypeId(operationMessageTypeEntity.getParentId());
                userMessageSettingEntity.setMainTypeName(operationMessageTypeEntity.getParentName());
                userMessageSettingEntity.setSendEmailSetting(operationMessageTypeEntity.getSendEmailDefaultSetting());
                userMessageSettingEntity.setSendMailSetting(operationMessageTypeEntity.getSendMailDefaultSetting());
                userMessageSettingEntity.setSendMobileSetting(operationMessageTypeEntity.getSendMobileDefaultSetting());
                userMessageSettingEntity.setCanEdit(operationMessageTypeEntity.getCanEdit());
                userMessageSettingEntity.setStatus(MessageConstants.STATUS_YES);
                List<UserMessageSettingEntity> userMessageSettingEntities = new ArrayList<>();
                userMessageSettingEntities.add(userMessageSettingEntity);
                log.info("user_message_setting表增加记录，{}",userMessageSettingEntities.toArray());
                //user_message_setting增加记录
                userMessageMapper.addUserMessageSetting(userMessageSettingEntities);
                //更新联系人到user_message_receiver_setting表

                for(UserMessageReceiverSettingEntity s : addList){
                    UserMessageReceiverSettingEntity userMsgReceiverSettingEntity = new UserMessageReceiverSettingEntity();
                    userMsgReceiverSettingEntity.setMessageSettingId(userMessageSettingEntity.getId());
                    userMsgReceiverSettingEntity.setReceiverId(s.getId());
                    userMsgReceiverSettingEntity.setId(BigInteger.valueOf(IdWorker.getNextId()));
                    userMsgReceiverSettingEntity.setCreatedTime(new Date());
                    userMsgReceiverSettingEntity.setUpdatedTime(new Date());
                    userMsgReceiverSettingEntity.setCreatedUserId(createdUserId);
                    userMsgReceiverSettingEntity.setStatus(s.getStatus());
                    userMsgAddEntities.add(userMsgReceiverSettingEntity);
                    messageSettingAddDTO.setUserMsgAddEntities(userMsgAddEntities);
                }
            }
            return messageSettingAddDTO;
    }

    /**
     *
     * @param messageTypeId
     * @param userMsgReceiverSettingVo
     * @return
     */

    @Transactional(rollbackFor = Exception.class)
    public ResponseBean updateMsgReceiverByType(String messageTypeId,UserMsgReceiverSettingVo userMsgReceiverSettingVo) {
        ResponseBean responseBean = new ResponseBean();
        User user = SecurityContextUtil.getLoginUser();
        String createdUserId = user.getId();
        Integer typeLevel = userMessageMapper.getTypeLevelById(StringUtils.isNotBlank(messageTypeId)?new BigInteger(messageTypeId):userMsgReceiverSettingVo.getMessageId());
        if(typeLevel!=null){
            if(typeLevel==1){
                Map map = new HashMap();
                map.put("createdUserId",createdUserId);
                map.put("updatedTime",new Date());
                map.put("sendMailSetting",userMsgReceiverSettingVo.getSendMailSetting());
                map.put("sendEmailSetting",userMsgReceiverSettingVo.getSendEmailSetting());
                map.put("sendMobileSetting",userMsgReceiverSettingVo.getSendMobileSetting());
                map.put("messageTypeId",StringUtils.isNotBlank(messageTypeId)?new BigInteger(messageTypeId):userMsgReceiverSettingVo.getMessageId());
                int success = userMessageMapper.updateMsgReceiverSettingByType(map);
                if(success==0){
                    responseBean.setCode("3");
                    responseBean.setMessage("接收方式设置失败");
                }
            }else{
                Map map = new HashMap();
                map.put("createdUserId",createdUserId);
                map.put("updatedTime",new Date());
                map.put("sendMailSetting",userMsgReceiverSettingVo.getSendMailSetting().toUpperCase());
                map.put("sendEmailSetting",userMsgReceiverSettingVo.getSendEmailSetting().toUpperCase());
                map.put("sendMobileSetting",userMsgReceiverSettingVo.getSendMobileSetting().toUpperCase());
                map.put("mainTypeId",StringUtils.isNotBlank(messageTypeId)?new BigInteger(messageTypeId):userMsgReceiverSettingVo.getMessageId());
                int success = userMessageMapper.updateMsgReceiverSettingByType(map);
                if(success==0){
                    responseBean.setCode("3");
                    responseBean.setMessage("接收方式设置失败");
                }
            }
            responseBean.setCode("0");
            responseBean.setMessage("接收方式设置成功");
            return responseBean;
        }else{
            ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.NON_EXISTTEN_MESSAGE_TYPE);
            responseBean.setCode(errorCodeEntity.getErrorCode());
            responseBean.setMessage(errorCodeEntity.getErrorMessage());
            return responseBean;
        }
    }

    public ResponseBean updateUserMsgSettingById(List<UserMsgReceiverSettingVo> userMsgReceiverSettingVoList){
        ResponseBean responseBean = new ResponseBean();
        List<BigInteger> failureList = new ArrayList<>();
        if(userMsgReceiverSettingVoList.size()==0){
            responseBean.setCode(ResponseCode.RESPONSE_CODE_FAILURE);
            responseBean.setMessage(ResponseCode.REQUEST_PARAM_ERROR);
        }else{
            for(UserMsgReceiverSettingVo userMsgReceiverSettingVo : userMsgReceiverSettingVoList){
                //记录更新时间
                Integer result = userMessageMapper.updateUserMsgSettingById(userMsgReceiverSettingVo,new Date());
                if(result!=1){
                    failureList.add(userMsgReceiverSettingVo.getMessageId());
                }
            }
            if(failureList.size()==0){
                responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
                responseBean.setMessage("接收方式设置成功！");
            }else{
                ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.UPDATE_MESSAGE_SEND_SETTING_FAILURE);
                responseBean.setCode(errorCodeEntity.getErrorCode());
                responseBean.setMessage(errorCodeEntity.getErrorMessage());
                responseBean.setResult(Arrays.toString(failureList.toArray()));
            }
        }
        return responseBean;
    }
}
