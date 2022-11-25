package com.tarento.analytics.dto;

import java.util.List;

public class DashboardData {	

	private Info info;
	private Layout layout;
	private Filters filters;
	private Config config;
	private Widgets widgets;
	private List<RoleDto> access;
    
	public Info getInfo() {
		return info;
	}
	public void setInfo(Info info) {
		this.info = info;
	}
	public Layout getLayout() {
		return layout;
	}
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	public Filters getFilters() {
		return filters;
	}
	public void setFilters(Filters filters) {
		this.filters = filters;
	}
	public Config getConfig() {
		return config;
	}
	public void setConfig(Config config) {
		this.config = config;
	}
	public Widgets getWidgets() {
		return widgets;
	}
	public void setWidgets(Widgets widgets) {
		this.widgets = widgets;
	}
	public List<RoleDto> getAccess() {
		return access;
	}
	public void setAccess(List<RoleDto> access) {
		this.access = access;
	}
    

}
