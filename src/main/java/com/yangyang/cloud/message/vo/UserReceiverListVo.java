package com.yangyang.cloud.message.vo;

import lombok.Data;

import java.util.List;

/**
 * description: todo
 *
 * @author renhaixiang
 * @version 1.0
 * @date 2019/4/25 17:39
 **/
@Data
public class UserReceiverListVo {
    /**
     * 存放查询的结果集
     */
    private List<?> data;
    /**
     * 接收人数量是否达到最大值
     */
    private Boolean receiverMax;
}
