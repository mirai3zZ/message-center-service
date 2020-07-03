package com.yangyang.cloud.message.bean;

import lombok.Data;

import java.math.BigInteger;

/**
 *
 * desc: Only return recipient information, other information hidden
 *
 * @author chenxinyi
 * @date 2018/11/1 16:05
 */
@Data
public class UserMessageReceiverInfoEntity {
    /**
     * description: 唯一标识
     **/
    private BigInteger id;
    /**
     * description: 接收人名称
     **/
    private String receiverName;
    /**
     * description: 接收人邮箱
     **/
    private String receiverEmail;
    /**
     * description: 接收人手机
     **/
    private String receiverMobile;
    /**
     * description: 职位
     **/
    private String receiverJob;
    /**
     * description: 选中状态
     **/
    private String status;
    /**
     * description: 邮件是否验证
     **/
    private String emailCheckPass;
    /**
     * description: 短信是否验证
     **/
    private String mobileCheckPass;
    /**
     * descripttion:是否为账号联系人
     */
    private String isUserSelf;

}
