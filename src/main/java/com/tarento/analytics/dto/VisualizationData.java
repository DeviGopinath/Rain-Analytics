package com.tarento.analytics.dto;

public class VisualizationData {
	
	private Info info;
	private QueryData data;
	private Access access;
	private Object dashboards;
	private AdditionalConfig additionalConfig;
    
	public AdditionalConfig getAdditionalConfig() {
		return additionalConfig;
	}
	public void setAdditionalConfig(AdditionalConfig additionalConfig) {
		this.additionalConfig = additionalConfig;
	}
	public Info getInfo() {
		return info;
	}
	public void setInfo(Info info) {
		this.info = info;
	}
	public QueryData getData() {
		return data;
	}
	public void setData(QueryData data) {
		this.data = data;
	}
	public Access getAccess() {
		return access;
	}
	public void setAccess(Access access) {
		this.access = access;
	}
	public Object getDashboards() {
		return dashboards;
	}
	public void setDashboards(Object dashboards) {
		this.dashboards = dashboards;
	}

}
