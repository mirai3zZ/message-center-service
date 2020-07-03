package com.yangyang.cloud.message.vo;

import com.yangyang.cloud.common.bean.TableContainerBean;
import com.yangyang.cloud.fileoperation.bean.FileBean;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 *
 * @author: LiuYang01
 * @date: 2019/8/26 15:37
 */
@Data
public class MessageReleaseReceiversVo {
    @NotBlank(message = "模版ID不能为空")
    private String templateId;
    /**
     * 消息接收人（“all”/id/email/mobile）
     */
    @NotBlank
    private String receivers;
    private String[] emailParams;
    private String[] titleEmailParams;
    private String[] smsParams;

    private List<TableContainerBean> tableContainerBeans;

    private List<FileBean> files;
}
