package com.yangyang.cloud.message.vo;

import com.yangyang.cloud.message.bean.UserMessageSettingEntity;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

/**
 * description:获取用户所有消息设置返回数据
 *
 * @author: LiuYang01
 * @date: 2019/10/9 17:10
 */
@Data
public class UserMessageSettingVo {
    private BigInteger messageTypeId;
    private String messageTypeName;
    private String sendEmailSetting;
    private String sendMailSetting;
    private String sendMobileSetting;
    private List<UserMessageSettingEntity> children;
}
