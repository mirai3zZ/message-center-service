package com.yangyang.cloud.message.bean;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

/**
 * description:common_user_message
 *
 * @author: LiuYang01
 * @date: 2019/11/23 16:48
 */
@Data
public class UpdUserForwardHistoryBean {
    private BigInteger id;
    private String content;
    private Date createdTime;
    private String status;
    private Integer sendErrorTimes;
    private String type;
    private String smsFailedReason;
}
