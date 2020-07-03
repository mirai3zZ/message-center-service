package com.yangyang.cloud.message.controller;

import com.yangyang.cloud.aop.adminControl;
import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.common.ResponseCode;
import com.yangyang.cloud.common.bean.User;
import com.yangyang.cloud.common.exception.model.ErrorCodeEntity;
import com.yangyang.cloud.common.paginate.Page;
import com.yangyang.cloud.common.redis.RedisUtil;
import com.yangyang.cloud.keycloak.SecurityContextUtil;
import com.yangyang.cloud.message.bean.OperationMessageEntity;
import com.yangyang.cloud.message.bean.UserMessageReceiverEntity;
import com.yangyang.cloud.message.bean.UserMessageSettingEntity;
import com.yangyang.cloud.message.common.MessageConstants;
import com.yangyang.cloud.message.common.MessageErrorCode;
import com.yangyang.cloud.message.service.MessageService;
import com.yangyang.cloud.message.service.UserMessageService;
import com.yangyang.cloud.message.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * desc: 消息相关接口
 *
 * @author chenxinyi
 * @date 2018/11/15 8:58
 */
@RestController
@Validated
@RequestMapping("/operation/message")
@Slf4j
public class UserMessageController {
    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MessageErrorCode messageErrorCode;

    /**
     * 功能描述 消息接收管理列表
     *
     * @author chenxinyi
     * @date 2018/9/29 14:26
     */
    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public ResponseBean getUserSettingList() {
        return userMessageService.getUserSetting();
    }

    /**
     * 功能描述 消息类型接收设置接口
     *
     * @author chenxinyi
     * @date 2018/9/29 14:26
     */
    @RequestMapping(value = "/setting", method = RequestMethod.PUT)
    public ResponseBean editUserSetting(@Valid @RequestBody List<UserMessageSettingEntity> userMessageSettingEntities) {
        return userMessageService.editUserSetting(userMessageSettingEntities);
    }

    /**
     * 功能描述 接收人修改接口
     *
     * @author chenxinyi
     * @date 2018/9/29 14:26
     */
    @RequestMapping(value = "/setting/{id}/receivers", method = RequestMethod.PUT)
    public ResponseBean editUserReceiver(@RequestBody UserMessageReceiverSettingVo userMessageReceiverSettingVo) {
        return userMessageService.editUserReceiver(userMessageReceiverSettingVo);
    }

    /**
     * 功能描述 消息设置恢复默认接口
     *
     * @author chenxinyi
     * @date 2018/9/29 14:26
     */
    @RequestMapping(value = "/setting", method = RequestMethod.POST)
    @Deprecated
    public List<UserMessageSettingEntity> resetUserReceiverSetting() {
        return userMessageService.resetUserReceiverSetting();
    }

    /**
     * 功能描述: 验证接收人手机号或邮箱接口
     *
     * @return ResponseBean
     */
    @CrossOrigin
    @GetMapping(value = "/receivers/verify")
    public ResponseBean verifyReceiverMessage(String a) {
        return userMessageService.verifyReceiverMessage(a);
    }

    /**
     * 功能描述:邀请接收人
     *
     * @return ResponseBean
     */
    @GetMapping("/receivers/invitation")
    public ResponseBean inviteReceivers(String receiverId, String mobile, String email) {
        return userMessageService.receiverInvite(receiverId, mobile, email);
    }


    /**
     * 未读消息(提供)
     *
     * @param page            Page
     * @param messageMainType String
     * @param size            String
     * @return ResponseBean
     */
    @GetMapping("/unread/{messageType}")
    public ResponseBean<HashMap<String, Object>> queryUnreadMessageForPage(@ModelAttribute Page page,
                                                                           @PathVariable(value = "messageType") String messageMainType,
                                                                           @RequestParam(value = "size", required = false) String size) {
        ResponseBean<HashMap<String, Object>> responseBean = new ResponseBean<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<>();
        if (MessageConstants.MESSAGE_GET_ALL.equals(messageMainType)){
            messageMainType = "";
        }
        if (StringUtils.isEmpty(size)) {
            page.setLength(MessageConstants.MESSAGE_DEFAULT_SIZE);
        } else if (StringUtils.isNumeric(size)) {
            // the max size is 50
            if (Integer.parseInt(size) > MessageConstants.MESSAGE_MAX_LIST) {
                page.setLength(MessageConstants.MESSAGE_MAX_LIST);
            }
            page.setLength(Integer.parseInt(size));
        }
        List<OperationMessageEntity> operationMessageEntityList = userMessageService.queryMessageForPage(page, messageMainType, true);
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        map.put("data", Optional.ofNullable(operationMessageEntityList).orElseGet(() -> new ArrayList<OperationMessageEntity>()));
        map.put("unReadCount", Optional.ofNullable(page.getiTotalDisplayRecords()).orElseGet(() -> MessageConstants.MESSAGE_NUM_ZERO));
        responseBean.setResult(map);
        responseBean.setMessage(MessageConstants.MESSAGE_UNREAD_QUERY_SUCCESS);
        return responseBean;
    }

    /**
     * 未读消息 、全部消息
     *
     * @param page
     * @param messageMainType all 全部消息
     * @return
     */
    @GetMapping("/unread")
    public ResponseBean<Page> queryUnreadMessageForPage(@ModelAttribute Page page,
                                                        @RequestParam(value = "messageType", required = true) String messageMainType) {
        if (MessageConstants.MESSAGE_GET_ALL.equals(messageMainType)){
            messageMainType = "";
        }
       // List<OperationMessageEntity> operationMessageEntityList = userMessageService.queryMessageForPage(page, messageMainType,true);
        List<OperationMessageEntity> operationMessageEntityList = userMessageService.queryValidMessageForPage(page, messageMainType, true);
        page.setData(operationMessageEntityList);
        ResponseBean<Page> responseBean = new ResponseBean<Page>();
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
        responseBean.setResult(page);
        return responseBean;
    }

    /**
     * 全部消息
     *
     * @param page
     * @param messageMainType
     * @return
     */
    @GetMapping
    public ResponseBean<Page> queryMessageForPage(@ModelAttribute Page page,
                                                  @RequestParam(value = "messageType", required = true) String messageMainType) {
        if (MessageConstants.MESSAGE_GET_ALL.equals(messageMainType)){
            messageMainType = "";
        }
       // List<OperationMessageEntity> allMessageEntityList = userMessageService.queryMessageForPage(page, messageMainType,false);
        List<OperationMessageEntity> allMessageEntityList= userMessageService.queryValidMessageForPage(page, messageMainType, false);
        page.setData(allMessageEntityList);
        ResponseBean<Page> responseBean = new ResponseBean<Page>();
        responseBean.setCode(ResponseCode.RESPONSE_CODE_SUCCESS);
        responseBean.setMessage(ResponseCode.RESPONSE_SUCCESS_MESSAGE);
        responseBean.setResult(page);
        return responseBean;
    }

    /**
     * desc:Recipient management list
     *
     * @return Recipient management list
     * @author renhaixiang
     * @date 2018/9/29 16:39
     **/
    @RequestMapping(value = "/receivers", method = RequestMethod.GET)
    public ResponseBean getReceiverList() {
        return userMessageService.getReceiverListByUserId(SecurityContextUtil.getLoginUser().getId());
    }

    /**
     * description: 接收人列表其他模块调用接口
     *
     * @param id 当前账户id
     * @return 接收人列表
     * @author renhaixiang
     * @date 2019/5/1 9:51
     **/
    @RequestMapping(value = "/inter/receivers/{id}", method = RequestMethod.GET)
    public ResponseBean getInterReceiverList(@PathVariable("id") String id) {
        return userMessageService.getReceiverListByUserId(id);
    }

    /**
     * description: 接收人管理  添加接收人
     *
     * @param userMessageReceiverEntity 接收人信息参数
     * @return responseBean 包含成功失败信息
     * @author renhaixiang
     * @date 2018/9/29 19:40
     **/
    @RequestMapping(value = "/receivers", method = RequestMethod.POST)
    public ResponseBean addMessageReceiver(@RequestBody @Validated UserMessageAddVo userMessageReceiverEntity) {
        return userMessageService.addMessageReceiver(userMessageReceiverEntity.getMessageTypeIds(), userMessageReceiverEntity);
    }

    /**
     * 修改接收人信息
     *
     * @param userMessageReceiverEntity UserMessageReceiverEntity
     * @return ResponseBean
     */
    @RequestMapping(value = "/receivers/{id}", method = RequestMethod.PUT)
    public ResponseBean updateMessageReceiver(@PathVariable("id") String id, @RequestBody @Valid UserMessageReceiverEntity userMessageReceiverEntity) {
        userMessageReceiverEntity.setId(new BigInteger(id));
        return userMessageService.updateMessageReceiver(userMessageReceiverEntity);
    }

    /**
     * desc: Delete receiver based on id
     *
     * @return ResponseBean
     */
    @RequestMapping(value = "/receivers/{id}", method = RequestMethod.DELETE)
    public ResponseBean removeMessageReceiver(@PathVariable String id) {
        return userMessageService.removeMessageReceiver(id);
    }


    /**
     * 查询消息分类
     *
     * @return ResponseBean<List<MessageTypeListVO>>
     */
    @GetMapping("/mainTypes")
    public ResponseBean<List<MessageTypeListVO>> typeList() {
        return messageService.typeList();
    }

    /**
     * 获取未读消息不同分类数量
     *
     * @return ResponseBean<List<MessageUnreadCountVO>>
     */
    @GetMapping("/unread/count")
    public ResponseBean<List<MessageUnreadCountVO>> unreadCount() {
        return messageService.unreadCount();
    }

    /**
     * DESC:send message
     *
     * @param messageReleaseVo
     * @return
     */
    @PostMapping
    public ResponseBean messageRelease(@Valid @RequestBody MessageReleaseVo messageReleaseVo) {
        return messageService.messageRelease(messageReleaseVo);
    }

    /**
     *description:send message with receivers
     *@author: LiuYang01
     *@date: 2019/8/26 15:28
     */
    @PostMapping("/batch")
    public ResponseBean messageRelease(@Valid @RequestBody MessageReleaseReceiversVo messageReleaseReceiversVo){
        return messageService.messageReleaseWithReceivers(messageReleaseReceiversVo);
    }

    /**
     *description:send email to group recipient
     *@author: LiuYang01
     *@date: 2020/3/9 10:55
     */
    @PostMapping("/batch/email")
    public ResponseBean messageGroupRelease(@Valid @RequestBody MessageReleaseReceiversVo messageReleaseReceiversVo){
        return messageService.messageGroupRelease(messageReleaseReceiversVo);
    }

    /**
     *description:自定义发件人邮箱的邮件群发
     *@author: LiuYang01
     *@date: 2020/5/13 14:19
     */
    @PostMapping("/batch/email/sender")
    public ResponseBean messageSenderEmailRelease(@Valid @RequestBody MessageReleaseSenderEmailVO messageReleaseSenderEmailVO){
        return messageService.messageSenderEmailRelease(messageReleaseSenderEmailVO);
    }

    /**
     *description:send sms Verification Code
     *@author: LiuYang01
     *@date: 2019/8/19 10:28
     */
    @RequestMapping(value = "/sms/verify", method = RequestMethod.POST)
    public ResponseBean smsVerifyCodeRelease(@RequestBody @Valid SmsVerifyContentVo smsVerifyContentVo){
        return messageService.smsVerifyCodeRelease(smsVerifyContentVo);
    }

    /**
     *description:Sms Verification Code Check
     *@author: LiuYang01
     *@date: 2019/8/20 17:29
     */
    @RequestMapping(value = "/sms/check/{mobile}/{verifyCode}", method = RequestMethod.GET)
    public ResponseBean smsVerifyCodeCheck(@PathVariable("mobile")String mobile,@PathVariable("verifyCode")String verifyCode ){
        ResponseBean responseBean = new ResponseBean();
        Object errorTimes = redisUtil.get(MessageConstants.CODE_PERMIT_ERROR_TIMES);
        log.info("验证码错误次数，errorTimes:{}",errorTimes);
        if(errorTimes!=null){
            int count=Integer.parseInt(errorTimes.toString());
            count +=1;
            redisUtil.set(MessageConstants.CODE_PERMIT_ERROR_TIMES,count);
            //计算短信验证码错误次数
            if(count>5){
                redisUtil.del(MessageConstants.REDIS_KEY+mobile,MessageConstants.CODE_PERMIT_ERROR_TIMES);
                ErrorCodeEntity errorCodeEntity = messageErrorCode.getMessageErrorMsg(MessageErrorCode.VERIFY_CODE_TIMES_ERROR);
                responseBean.setCode(errorCodeEntity.getErrorCode());
                responseBean.setMessage(errorCodeEntity.getErrorMessage());
                return responseBean;
            }else{
                return messageService.smsVerifyCodeCheck(verifyCode,mobile);
            }
        }else{
            return  messageService.smsVerifyCodeCheck(verifyCode,mobile);
        }
    }

    /**
     * DESC:send message 对手机和邮箱无处理
     *
     * @param messageReleaseVo
     * @return
     */
    @PostMapping("/entireties")
    public ResponseBean messageReleaseEntireties(@Valid @RequestBody MessageReleaseVo messageReleaseVo) {
        return messageService.messageReleaseEntireties(messageReleaseVo);
    }

    /**
     * 用户-删除消息
     *
     * @param markUnreadMessageVo MarkUnreadMessageVo
     * @return ResponseBean
     */
    @DeleteMapping("")
    public ResponseBean deleteMessage(@RequestBody MarkUnreadMessageVo markUnreadMessageVo) {
        return userMessageService.deleteMessage(markUnreadMessageVo.getIds());
    }

    /**
     * 标记已读
     *
     * @param markUnreadMessageVo MarkUnreadMessageVo
     * @return ResponseBean
     */
    @PatchMapping("/unread")
    public ResponseBean markAsRead(@RequestBody MarkUnreadMessageVo markUnreadMessageVo) {
        return userMessageService.markAsRead(markUnreadMessageVo);
    }

    /**
     * 批量添加消息接收人
     *
     * @param batchAddReceiverVo BatchAddReceiverVo
     * @return ResponseBean
     */
    @PostMapping("/setting/receivers")
    public ResponseBean receiverBatchAdd(@RequestBody BatchAddReceiverVo batchAddReceiverVo) {
        return userMessageService.receiverBatchAdd(batchAddReceiverVo.getSettingIds(), batchAddReceiverVo.getReceiversIds());
    }

    /**
     * 批量移除消息接收人
     *
     * @param batchAddReceiverVo BatchAddReceiverVo
     * @return ResponseBean
     */
    @PutMapping("/setting/receivers")
    public ResponseBean receiverBatchDelete(@RequestBody BatchAddReceiverVo batchAddReceiverVo) {
        return userMessageService.batchDeleteReceiver(batchAddReceiverVo.getSettingIds(), batchAddReceiverVo.getReceiversIds());
    }

    /**
     * description: 消息接收联系人列表接口（供内部调用）
     *
     * @return 接收人列表
     * @author renhaixiang
     * @date 2019/7/31 15:35
     **/
    @GetMapping(value = "/inner/receivers")
    public ResponseBean getInnerReceiverList() {
        return userMessageService.getReceiverListByUserId(SecurityContextUtil.getLoginUser().getId());
    }

    /**
     *description:通过联系人姓名查询消息接收人
     *@author: LiuYang01
     *@date: 2020/5/6 8:49
     */
    @GetMapping(value = "/inner/receiver")
    public ResponseBean getReceiverListByName(@RequestParam("receiverName") String receiverName){
        return  userMessageService.getReceiverListByName(receiverName);
    }

    /**
     * description: 添加消息接收联系人接口（供内部调用）
     *
     * @param userMessageReceiverEntity 接收人信息参数
     * @return responseBean 包含成功失败信息
     * @author renhaixiang
     * @date 2019/7/31 15:46
     **/
    @PostMapping(value = "/inner/receivers")
    public ResponseBean addInnerMessageReceiver(@RequestBody @Valid UserMessageAddVo userMessageReceiverEntity) {
        return userMessageService.addMessageReceiver(userMessageReceiverEntity.getMessageTypeIds(), userMessageReceiverEntity);
    }

    /**
     * description: 修改消息接收联系人接口（供内部调用）
     *
     * @author renhaixiang
     * @date 2019/7/31 16:13
     **/
    @PutMapping(value = "/inner/receivers/{id}")
    public ResponseBean updateInnerMessageReceiver(@PathVariable("id") String id, @RequestBody @Valid UserMessageReceiverEntity userMessageReceiverEntity) {
        userMessageReceiverEntity.setId(new BigInteger(id));
        return userMessageService.updateMessageReceiver(userMessageReceiverEntity);
    }

    /**
     * description: 删除消息接收联系人接口（供内部调用）
     *
     * @author renhaixiang
     * @date 2019/7/31 16:31
     **/
    @DeleteMapping(value = "/inner/receivers/{id}")
    public ResponseBean removeInnerMessageReceiver(@PathVariable String id) {
        return userMessageService.removeMessageReceiver(id);
    }

    /**
     *description:获取个人的消息类型和未读
     *@author: LiuYang01
     *@date: 2019/9/28 9:51
     */
    @GetMapping(value = "/types/unread")
    public ResponseBean getMessageTypesCount(){
        return userMessageService.getMessageTypesCount();
    }
    
    /**
     *description:获取用户所有消息设置
     *@author: LiuYang01
     *@date: 2019/10/9 15:47
     */
    @GetMapping(value = "/messages/types/settings")
    public ResponseBean getUserAllMessageSetting(){
        User user = SecurityContextUtil.getLoginUser();
        String userId = user.getId();
        return userMessageService.getUserAllMessageSetting(userId);
    }

    /**
     *description:恢复消息默认设置
     *@author: LiuYang01
     *@date: 2019/10/17 9:02
     */
    @PostMapping(value = "/messages/types/settings")
    public ResponseBean resetUserMsgSetting(){
        return userMessageService.resetUserMsgSetting();
    }
}
