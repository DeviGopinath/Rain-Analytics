package com.tarento.analytics.model;

import java.util.List;

public class ChartData {
	private Long id;
	private String vizType;
	private String headerName;
	private String visualizationCode;
	private String color;
	private List<Object> plots;
	private String module; 
    private String dataSource; 
    private String requestQueryMap; 
    private String dateRefField; 
    private String indexName; 
    private String esInstance; 
    private String aggrQuery; 
    
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getRequestQueryMap() {
		return requestQueryMap;
	}
	public void setRequestQueryMap(String requestQueryMap) {
		this.requestQueryMap = requestQueryMap;
	}
	public String getDateRefField() {
		return dateRefField;
	}
	public void setDateRefField(String dateRefField) {
		this.dateRefField = dateRefField;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getEsInstance() {
		return esInstance;
	}
	public void setEsInstance(String esInstance) {
		this.esInstance = esInstance;
	}
	public String getAggrQuery() {
		return aggrQuery;
	}
	public void setAggrQuery(String aggrQuery) {
		this.aggrQuery = aggrQuery;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVizType() {
		return vizType;
	}
	public void setVizType(String vizType) {
		this.vizType = vizType;
	}
	public String getHeaderName() {
		return headerName;
	}
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	public String getVisualizationCode() {
		return visualizationCode;
	}
	public void setVisualizationCode(String visualizationCode) {
		this.visualizationCode = visualizationCode;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public List<Object> getPlots() {
		return plots;
	}
	public void setPlots(List<Object> plots) {
		this.plots = plots;
	}
}
