package com.yangyang.cloud.message.vo;

import lombok.Data;

/**
 * description:获取用户登录id和接收人id
 *
 * @author: LiuYang01
 * @date: 2019/9/18 9:18
 */
@Data
public class UserMsgCreatedUserIdVo {
    private String createdUserId;
    private String id;
}
