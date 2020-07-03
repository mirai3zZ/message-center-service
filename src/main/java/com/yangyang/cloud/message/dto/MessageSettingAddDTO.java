package com.yangyang.cloud.message.dto;

import com.yangyang.cloud.message.bean.UserMessageReceiverSettingEntity;
import lombok.Data;

import java.util.List;

/**
 * description:消息设置列表增加或者更新
 *
 * @author: LiuYang01
 * @date: 2019/9/23 10:00
 */

@Data
public class MessageSettingAddDTO {

    private List<UserMessageReceiverSettingEntity> userMsgAddEntities;

    private List<UserMessageReceiverSettingEntity> userMessageReceiverSettingEntities;
}
