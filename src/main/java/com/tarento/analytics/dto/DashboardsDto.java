package com.tarento.analytics.dto;

import java.util.List;

public class DashboardsDto {
	
	private String title;
    private boolean isPinned;
    private List<DashboardData> data;
    private String type;
    private Long id;
    private boolean isActive;
	private String style;
	private String widgetTitle;
	private boolean showFilters;
	private boolean showWidgets;
	private boolean showWidgetTitle;
	private String filters;
	private String profile;
	private String dashboardId;
	private Long createdBy;
	private Long timestamp;
	private String visualizationCode;
	private String visualizations;

	public String getVisualizations() {
		return visualizations;
	}
	public void setVisualizations(String visualizations) {
		this.visualizations = visualizations;
	}
	public String getVisualizationCode() {
		return visualizationCode;
	}
	public void setVisualizationCode(String visualizationCode) {
		this.visualizationCode = visualizationCode;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public String getDashboardId() {
		return dashboardId;
	}
	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}
	public void setPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean getIsPinned() {
		return isPinned;
	}
	public void setIsPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}
	public List<DashboardData> getData() {
		return data;
	}
	public void setData(List<DashboardData> data) {
		this.data = data;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getWidgetTitle() {
		return widgetTitle;
	}
	public void setWidgetTitle(String widgetTitle) {
		this.widgetTitle = widgetTitle;
	}
	public boolean getShowFilters() {
		return showFilters;
	}
	public void setShowFilters(boolean showFilters) {
		this.showFilters = showFilters;
	}
	public boolean getShowWidgets() {
		return showWidgets;
	}
	public void setShowWidgets(boolean showWidgets) {
		this.showWidgets = showWidgets;
	}
	public boolean getShowWidgetTitle() {
		return showWidgetTitle;
	}
	public void setShowWidgetTitle(boolean showWidgetTitle) {
		this.showWidgetTitle = showWidgetTitle;
	}
	public String getFilters() {
		return filters;
	}
	public void setFilters(String filters) {
		this.filters = filters;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}

}
