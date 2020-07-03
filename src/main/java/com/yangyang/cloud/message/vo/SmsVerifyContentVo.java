package com.yangyang.cloud.message.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * description:短信验证信息
 *
 * @author: LiuYang01
 * @date: 2019/8/19 10:36
 */

@Data
public class SmsVerifyContentVo {

    /**
     *description:接收人手机号
     */
    @NotBlank
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$")
    private String mobile;

    /**
     *description:操作名
     */
    private String action;

    /**
     *description:验证码有效期
     */
    @NotNull
    private Integer validPeriod;

    /**
     *description:提示文本
     */
    private String point;
}
