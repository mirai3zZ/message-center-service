package com.yangyang.cloud.common.enums;

/**
 * @author daiyan
 */
public enum MessageTemplateTypeEnum {
	/**
	 * 站内信
	 */
	MAIL("mail", "站内信"),
	
	/**
	 * 邮件
	 */
	EMAIL("email", "邮件"),
	
	/**
	 * 短信
	 */
	SMS("sms", "短信");

	private String type;
	private String typeCH;
	
	private MessageTemplateTypeEnum (String type, String typeCH) {
		this.type = type;
		this.typeCH = typeCH;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeCH() {
		return typeCH;
	}

	public void setTypeCH(String typeCH) {
		this.typeCH = typeCH;
	}
}
