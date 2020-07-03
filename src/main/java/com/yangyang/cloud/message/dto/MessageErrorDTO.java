package com.yangyang.cloud.message.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

/**
 * description:
 *
 * @author: LiuYang01
 * @date: 2019/8/26 17:27
 */
@Data
public class MessageErrorDTO {
    @Email(message = "邮箱格式不正确")
    private String email;
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$", message = "手机格式不正确")
    private String mobile;
    private String errorMessage;
}
