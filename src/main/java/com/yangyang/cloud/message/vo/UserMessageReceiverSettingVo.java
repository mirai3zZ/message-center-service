package com.yangyang.cloud.message.vo;

import com.yangyang.cloud.message.bean.UserMessageReceiverSettingEntity;
import lombok.Data;

import java.math.BigInteger;

/**
 * 消息接收人设置vo
 *
 * @author chenxinyi
 * @date 2018/9/28 10:29
 */
@Data
public class UserMessageReceiverSettingVo {

    private BigInteger id;
    private UserMessageReceiverSettingEntity[] list;
}
