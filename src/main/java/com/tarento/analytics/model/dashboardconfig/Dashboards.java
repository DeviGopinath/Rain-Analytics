package com.tarento.analytics.model.dashboardconfig;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dashboards {

	@JsonProperty("id")
	private Long id;
	@JsonProperty("dashboardId")
	private String dashboardId;
	@JsonProperty("title")
	private String title;
	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("isActive")
	private boolean isActive;
	@JsonProperty("style")
	private String style;
	@JsonProperty("widgetTitle")
	private String widgetTitle;
	@JsonProperty("showFilters")
	private boolean showFilters;
	@JsonProperty("showWidgets")
	private boolean showWidgets;
	@JsonProperty("showWidgetTitle")
	private boolean showWidgetTitle;
	@JsonProperty("filters")
	private String filters;
	@JsonProperty("widgetCharts")
	private String widgetCharts;
	@JsonProperty("visualizations")
	private String visualizations;
	@JsonProperty("profile")
	private String profile;
	@JsonProperty("createdBy")
	private Long createdBy;
	@JsonProperty("isPinned")
	private boolean isPinned;
	@JsonProperty("layoutConfig")
	private String layoutConfig;
	@JsonProperty("type")
	private String type;
	@JsonProperty("showDateFilter")
	private boolean showDateFilter;
	@JsonProperty("dashboardConfig")
	private String dashboardConfig;
	@JsonProperty("access")
	private String access;
	@JsonProperty("timestamp")
	private Long timestamp;
	@JsonProperty("visualizationCode")
	private String visualizationCode;
	
	public String getVisualizationCode() {
		return visualizationCode;
	}

	public void setVisualizationCode(String visualizationCode) {
		this.visualizationCode = visualizationCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getWidgetCharts() {
		return widgetCharts;
	}

	public void setWidgetCharts(String widgetCharts) {
		this.widgetCharts = widgetCharts;
	}

	public String getVisualizations() {
		return visualizations;
	}

	public void setVisualizations(String visualizations) {
		this.visualizations = visualizations;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public boolean getShowDateFilter() {
		return showDateFilter;
	}

	public void setShowDateFilter(boolean showDateFilter) {
		this.showDateFilter = showDateFilter;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public boolean getIsPinned() {
		return isPinned;
	}

	public void setIsPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLayoutConfig() {
		return layoutConfig;
	}

	public void setLayoutConfig(String layoutConfig) {
		this.layoutConfig = layoutConfig;
	}	
	
	public String getDashboardConfig() {
		return dashboardConfig;
	}

	public void setDashboardConfig(String dashboardConfig) {
		this.dashboardConfig = dashboardConfig;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	
}