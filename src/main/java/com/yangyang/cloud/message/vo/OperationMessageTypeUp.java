package com.yangyang.cloud.message.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@EqualsAndHashCode(of = {"id"})
public class OperationMessageTypeUp {
    @NotBlank(message="id不能为空" )
    private String id;
    @NotBlank(message="邮件不能为空" )
    private String switchEmail;
    @NotBlank(message="短信不能为空" )
    private String switchMail;
    @NotBlank(message="手机不能为空" )
    private String switchMobile;
    @NotBlank
    private String canEdit;
    private Date updatedTime = new Date();
    private String updatedUserName;
    private String updateUserId;

}
