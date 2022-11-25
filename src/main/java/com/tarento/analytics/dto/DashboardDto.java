package com.tarento.analytics.dto;

import java.util.List;

import com.tarento.analytics.model.dashboardconfig.DashboardData;

public class DashboardDto {
	private Long id; 
	private String title; 
	private Long timestamp; 
	private Boolean isPinned; 
	private String type; 
	private List<DashboardData> data;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public Boolean getIsPinned() {
		return isPinned;
	}
	public void setIsPinned(Boolean isPinned) {
		this.isPinned = isPinned;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<DashboardData> getData() {
		return data;
	}
	public void setData(List<DashboardData> data) {
		this.data = data;
	} 
	
	
	
	
}
