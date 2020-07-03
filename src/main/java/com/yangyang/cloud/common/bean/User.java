package com.yangyang.cloud.common.bean;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class User {

	private String id;//内码、唯一标识
	private String name;//loginName
	private String displayName;//familyName+givenName
	private String email;
	private Set<String> roles = new HashSet<>();
	
}
