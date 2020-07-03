package com.yangyang.cloud.message.vo;

import com.yangyang.cloud.common.bean.TableContainerBean;
import com.yangyang.cloud.fileoperation.bean.FileBean;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author yuanye
 * @date 2018/10/31
 * Desc: message release params
 */
@Data
public class MessageReleaseVo {
    @NotBlank(message = "模版ID不能为空")
    private String templateId;
    private String receiverId;
    @Email(message = "邮箱格式不正确")
    private String email;
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$", message = "手机格式不正确")
    private String mobile;
    private String[] mailParams;
    private String[] emailParams;
    private String[] titleMailParams;
    private String[] titleEmailParams;
    private String[] smsParams;

    private List<TableContainerBean> tableContainerBeans;

    private List<FileBean> files;

}
