package com.yangyang.cloud.fileoperation.bean;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;
@Data
public class OperationAttachmentInfo {
     private BigInteger id;
    /** 模块代码*/
    private String  moduleCode;
    /** */
    private String  moduleId;
    /** 原始附件名称*/
    private String  originalName;
    /** 附件保存的名称*/
    private String  fileName;
    /** 附件后缀 doc,jpg'*/
    private String  fileSuffix;
    /** url路径*/
    private String  fuleUrl;
    /** 附件大小字节数（byte）*/
    private Integer  fileSize;
    /** 创建人*/
    private String  createdUserId;

    private String  createdUserName;
    /** 创建时间*/
    private Date createdTime;
    /**
     * 附件属性
     */
    private String fileAttribute;
    /**
     * 扩展字段
     */
    private String fileAttribute2;

    /**
     * 附件关联的表名
     */
    private String tableName;

}
