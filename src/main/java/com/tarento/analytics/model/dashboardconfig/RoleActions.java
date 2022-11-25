package com.tarento.analytics.model.dashboardconfig;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RoleActions {
	
	@JsonProperty("roleId")
	private Long roleId;
	@JsonProperty("actionId")
	private Long actionId;
	@JsonProperty("roleIds")
	private List<Long> roleIds;
	@JsonProperty("actionIds")
	private List<Long> actionIds;
	
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getActionId() {
		return actionId;
	}
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
	public List<Long> getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}
	public List<Long> getActionIds() {
		return actionIds;
	}
	public void setActionIds(List<Long> actionIds) {
		this.actionIds = actionIds;
	}		

}
