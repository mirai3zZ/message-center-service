package com.yangyang.cloud.message.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigInteger;

/**
 * description:消息类型和接收方式
 *
 * @author: LiuYang01
 * @date: 2019/9/18 19:40
 */
@Data
public class UserMsgReceiverSettingVo {
    //父类型id或者消息类型id
    private BigInteger messageId;

    @NotBlank
    @Pattern(regexp ="^(Y|N|y|n)$" ,message = "只能输入Y和N")
    private String sendMailSetting;
    @NotBlank
    @Pattern(regexp ="^(Y|N|y|n)$" ,message = "只能输入Y和N")
    private String sendEmailSetting;
    @NotBlank
    @Pattern(regexp ="^(Y|N|y|n)$" ,message = "只能输入Y和N")
    private String sendMobileSetting;
}
