package com.tarento.analytics.model;

import java.util.List;

public class ColData {
	private String chartType;
	private List <ChartData> chartData;
	
	public String getChartType() {
		return chartType;
	}
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
	public List<ChartData> getChartData() {
		return chartData;
	}
	public void setChartData(List<ChartData> chartData) {
		this.chartData = chartData;
	}
}
