package com.yangyang.cloud.message.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigInteger;

/**
 * description:全部消息和未读消息对应消息类型包含消息数量
 *
 * @author: LiuYang01
 * @date: 2019/9/28 16:05
 */
@Data
public class MessageTypeCountVo {
    /**
     * 父消息类型+消息数量
     */
    @NotBlank
    private String mainTypeName;
    @NotBlank
    private String mainTypeId;
}
