package com.yangyang.cloud.message.bean;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

/**
 *
 * 功能描述: 表operation_message_type 对应实体
 *
 * @author chenxinyi
 * @date 2018/9/29 17:12
 */
@Data
public class OperationMessageTypeEntity {

    /**
     * description: 唯一标识
     **/
    private BigInteger id;
    /**
     * description: 创建时间
     **/

    private Date createdTime;
    /**
     * description: 创建人id
     **/
    private String createdUserId;
    /**
     * description: 创建人名称
     **/
    private String createdUserName;
    /**
     * description: 更新时间
     **/
    private Date updatedTime;
    /**
     * description: 更新人id
     **/
    private String updatedUserId;
    /**
     * description: 更新人名称
     **/
    private String updatedUserName;
    /**
     * description: 类型名称
     **/
    private String typeName;
    /**
     * description: 类型级别
     **/
    private int typeLevel;  // 分类（mainType）的typeLevel=0，类型的typeLevel=1
    /**
     * description: 站内信是否发
     **/
    private String sendMailDefaultSetting;
    /**
     * description: 邮件是否发放
     **/
    private String sendEmailDefaultSetting;
    /**
     * description: 短信是否发放
     **/
    private String sendMobileDefaultSetting;
    /**
     * description: 主分类ID
     **/
    private BigInteger parentId;  // 分类（mainType）的parentId是其本身的id，类型的parentId是其分类的id
    /**
     * description: 主分类ID
     **/
    private String parentName;
    /**
     * description: 是否有效（Y/N）
     **/
    private String status;
    private  String canEdit;
    private String usingObjectType;
}
