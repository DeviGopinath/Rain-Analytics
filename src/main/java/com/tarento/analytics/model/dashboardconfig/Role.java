package com.tarento.analytics.model.dashboardconfig;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Role {
	
	@JsonProperty("id")
	private Long id;
	@JsonProperty("roleName")
	private String roleName;
	@JsonProperty("code")
	private List<String> code;
	@JsonProperty("description")
	private String description;
	@JsonProperty("isSuperAdmin")
	private Boolean isSuperAdmin;
	@JsonProperty("isOrgAdmin")
	private Boolean isOrgAdmin;
	@JsonProperty("orgId")
	private Long orgId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public List<String> getCode() {
		return code;
	}
	public void setCode(List<String> code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}
	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	public Boolean getIsOrgAdmin() {
		return isOrgAdmin;
	}
	public void setIsOrgAdmin(Boolean isOrgAdmin) {
		this.isOrgAdmin = isOrgAdmin;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
