package com.tarento.analytics.model.dashboardconfig;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRole {
	
	@JsonProperty("userId")
	private Integer userId;
	@JsonProperty("roleId")
	private Integer roleId;
	@JsonProperty("userIds")
	private List<Integer> userIds;
	@JsonProperty("roleIds")
	private List<Integer> roleIds;
	@JsonProperty("orgId")
	private Integer orgId;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public List<Integer> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}
	public List<Integer> getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(List<Integer> roleIds) {
		this.roleIds = roleIds;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	
	
	

}
