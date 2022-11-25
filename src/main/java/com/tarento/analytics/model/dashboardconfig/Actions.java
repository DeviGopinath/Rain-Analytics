package com.tarento.analytics.model.dashboardconfig;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Actions {
	
	@JsonProperty("id")
	private Long id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("displayName")
	private String displayName;
	@JsonProperty("url")
	private String url;
	@JsonProperty("serviceCode")
	private String serviceCode;
	@JsonProperty("enabled")
	private Boolean enabled;
	@JsonProperty("createdUser")
	private long createdUser;
	@JsonProperty("createdDate")
	private long createdDate;	
	@JsonProperty("updatedUser")
	private long updatedUser;
	@JsonProperty("updatedDate")
	private long updatedDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public long getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(long createdUser) {
		this.createdUser = createdUser;
	}
	public long getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}
	public long getUpdatedUser() {
		return updatedUser;
	}
	public void setUpdatedUser(long updatedUser) {
		this.updatedUser = updatedUser;
	}
	public long getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(long updatedDate) {
		this.updatedDate = updatedDate;
	}

}
