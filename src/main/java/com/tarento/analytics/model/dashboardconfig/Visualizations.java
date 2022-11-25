package com.tarento.analytics.model.dashboardconfig;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Visualizations {

	@JsonProperty("id")
	private Long id;
	@JsonProperty("visualizationCode")
	private String visualizationCode;
	@JsonProperty("chartName")
	private String chartName;
	@JsonProperty("description")
	private String description;
	@JsonProperty("queries")
	private String queries;
	@JsonProperty("chartType")
	private String chartType;
	@JsonProperty("isDecimal")
	private boolean isDecimal;
	@JsonProperty("createdBy")
	private Long createdBy;
	@JsonProperty("isPinned")
	private boolean isPinned;
	@JsonProperty("dashboards")
	private Object dashboards;
	@JsonProperty("type")
	private String type;
	@JsonProperty("timestamp")
	private Long timestamp;
	@JsonProperty("config")
	private String config;
	
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVisualizationCode() {
		return visualizationCode;
	}
	public void setVisualizationCode(String visualizationCode) {
		this.visualizationCode = visualizationCode;
	}
	public String getChartName() {
		return chartName;
	}
	public void setChartName(String chartName) {
		this.chartName = chartName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getQueries() {
		return queries;
	}
	public void setQueries(String queries) {
		this.queries = queries;
	}
	public String getChartType() {
		return chartType;
	}
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
	public boolean getIsDecimal() {
		return isDecimal;
	}
	public void setIsDecimal(boolean isDecimal) {
		this.isDecimal = isDecimal;
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
	public Object getDashboards() {
		return dashboards;
	}
	public void setDashboards(Object dashboards) {
		this.dashboards = dashboards;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
