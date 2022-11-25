package com.tarento.analytics.model.dashboardconfig;

import com.tarento.analytics.dto.Info;
import com.tarento.analytics.model.Layout;

public class DashboardData {
	private Info info; 
	private Layout layout;
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
	
}
