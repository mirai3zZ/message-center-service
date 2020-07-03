package com.yangyang.cloud.message.dto;

import com.yangyang.cloud.common.HtmlCompressor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;


@Data
public class MessageSendDTO {
    /**
     * 消息接收人（“all”/id/email/mobile）
     */
    @NotBlank
    private String receivers;
    /**
     * 消息分类
     */
    @NotNull
    private BigInteger mainTypeId;
    /**
     * 消息类型
     */
    @NotNull
    private BigInteger typeId;
    /**
     * 发送方式（账号ID、联系方式）
     */
    @NotBlank
    private String sendTarget;
    /**
     * 短信消息内容
     */
    private String smsContent;
    /**
     * 站内信标题
     */
    private String mailTitle;

    public void setMailContent(String mailContent) throws Exception{
        this.mailContent = HtmlCompressor.compress(mailContent);
    }

    public void setEmailContent (String emailContent) throws Exception{
        this.emailContent = HtmlCompressor.compress(emailContent);
    }

    /**
     * 站内信内容
     */
    private String mailContent;
    /**
     * 邮件主题
     */
    private String emailSubject;
    /**
     * 邮件内容
     */
    private String emailContent;
    /**
     * 选中Y,其他N
     */
    private String mailMessage;
    /**
     * 选中Y,其他N
     */
    private String emailMessage;
    /**
     * 选中Y,其他N
     */
    private String smsMessage;
}
