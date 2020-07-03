package com.yangyang.cloud.common.enums;

/**
 * problemCatigory and problemType's property
 * @author daiyan
 */
public enum ScopeEnum {
	/**
	 * 对客户
	 */
	EXTERNAL("external"),
	
	/**
	 * 仅对内部
	 */
	INNER("inner");
	
	private String scopeName;
	
	private ScopeEnum(String scopeName) {
		this.scopeName = scopeName;
	}

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}
}
