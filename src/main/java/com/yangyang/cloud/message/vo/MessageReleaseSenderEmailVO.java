package com.yangyang.cloud.message.vo;

import com.yangyang.cloud.common.bean.TableContainerBean;
import com.yangyang.cloud.fileoperation.bean.FileBean;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * description:增加发件人发送实体
 *
 * @author: LiuYang01
 * @date: 2020/5/13 14:24
 */
@Data
public class MessageReleaseSenderEmailVO {
    @NotBlank(message = "模版ID不能为空")
    private String templateId;
    /**
     * 消息接收人（“all”/id/email/mobile）
     */
    @NotBlank
    private String receivers;
    private String[] emailParams;
    private String[] titleEmailParams;

    /**
     * 发件人邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String senderEmail;
    /**
     * 邮箱密码
     */
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,18}$",message = "密码格式不正确，密码必须包含字母、数字和特殊字符，且在8-18位之间")
    private String emailPassword;

    /**
     * 邮箱别名
     */
    private String nickName;

    private List<TableContainerBean> tableContainerBeans;

    private List<FileBean> files;
}
