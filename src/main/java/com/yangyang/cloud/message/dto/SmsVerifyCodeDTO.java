package com.yangyang.cloud.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigInteger;
import java.util.Date;

/**
 * description:短信验证码返回实体
 *
 * @author: LiuYang01
 * @date: 2019/8/21 14:33
 */
@Data
public class SmsVerifyCodeDTO {

    @NotBlank
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$")
    private String mobile;

    private String status;

    private BigInteger messageId;

    private String verificationCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String sendTime;

    @NotBlank
    private Integer validPeriod;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String expiresTime;
}
