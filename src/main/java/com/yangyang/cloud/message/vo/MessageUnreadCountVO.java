package com.yangyang.cloud.message.vo;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author daiyan
 */
@Data
public class MessageUnreadCountVO {
	private String mainTypeName;
	private BigInteger mainTypeId;
	private Integer mainTypeNumber = 0;
}
