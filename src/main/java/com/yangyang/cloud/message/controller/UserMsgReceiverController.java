package com.yangyang.cloud.message.controller;

import com.yangyang.cloud.common.ResponseBean;
import com.yangyang.cloud.message.service.UserMsgReceiverService;
import com.yangyang.cloud.message.vo.UserMessageReceiverSettingVo;
import com.yangyang.cloud.message.vo.UserMsgReceiverSettingVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * description:
 *
 * @author: LiuYang01
 * @date: 2019/9/18 18:12
 */
@RestController
@Validated
@RequestMapping("/operation/message")
@Slf4j
public class UserMsgReceiverController {

    @Autowired
    private UserMsgReceiverService userMsgReceiverService;

    /**
     *description:通过消息联系人id获取联系方式
     *@author: LiuYang01
     *@date: 2019/9/13 14:41
     *@param:id
     *@return
     */
    @GetMapping("/receiver")
    public ResponseBean getReceiverContactById(@RequestParam("receiverId") String receiverId){
        return userMsgReceiverService.getReceiverContactById(receiverId);
    }

    /**
     *description:根据消息类型设置消息接收人
     *@author: LiuYang01
     *@date: 2019/9/18 17:21
     */
    @PutMapping(value = "/inner/{messageTypeId}/setting/receivers")
    public ResponseBean getMsgReceiverByType(@PathVariable ("messageTypeId") String messageTypeId,@RequestBody @Valid UserMessageReceiverSettingVo userMessageReceiverSettingVo){
        return userMsgReceiverService.getMsgReceiverByType(messageTypeId,userMessageReceiverSettingVo);
    }

    /**
     *description:根据消息类型修改消息接收方式接口
     *@author: LiuYang01
     *@date: 2019/9/18 19:35
     */
    @PutMapping(value = "/inner/{messageTypeId}/setting")
    public ResponseBean updateMsgReceiverByType(@PathVariable ("messageTypeId") String messageTypeId,@RequestBody @Valid UserMsgReceiverSettingVo userMsgReceiverSettingVo){
        return userMsgReceiverService.updateMsgReceiverByType(messageTypeId,userMsgReceiverSettingVo);
    }

    /**
     *description:根据messageSettingId修改消息接收方式
     *@author: LiuYang01
     *@date: 2019/10/10 14:56
     */
    @PostMapping(value = "/inner/types/setting")
    public ResponseBean updateUserMsgSettingById(@RequestBody @Valid List<UserMsgReceiverSettingVo> userMsgReceiverSettingVoList){
        return userMsgReceiverService.updateUserMsgSettingById(userMsgReceiverSettingVoList);
    }
}
