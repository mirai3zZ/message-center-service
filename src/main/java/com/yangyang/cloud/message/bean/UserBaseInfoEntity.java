package com.yangyang.cloud.message.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author daiyan
 */
public class UserBaseInfoEntity implements Serializable {
	private String id;
	private String email;
	private String passwd;
	private String userType;
	private String status;
	private String groupsId;
	private String roleId;
	private String mobile;
	private String alias;
	private Date createdTime;
	private Date updatedTime;
	private Integer userLevel;
	private String parentUserid;
	private Long provinceId;
	private Long cityId;
	private String authType;
	private Date lastLoginTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGroupsId() {
		return groupsId;
	}
	public void setGroupsId(String groupsId) {
		this.groupsId = groupsId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public Integer getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}
	public String getParentUserid() {
		return parentUserid;
	}
	public void setParentUserid(String parentUserid) {
		this.parentUserid = parentUserid;
	}
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public String getAuthType() {
		return authType;
	}
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	@Override
	public String toString() {
		return "UserBaseInfoEntity [id=" + id + ", email=" + email + ", passwd=" + passwd + ", userType=" + userType
				+ ", status=" + status + ", groupsId=" + groupsId + ", roleId=" + roleId + ", mobile=" + mobile
				+ ", alias=" + alias + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime + ", userLevel="
				+ userLevel + ", parentUserid=" + parentUserid + ", provinceId=" + provinceId + ", cityId=" + cityId
				+ ", authType=" + authType + ", lastLoginTime=" + lastLoginTime + "]";
	}
	
}
