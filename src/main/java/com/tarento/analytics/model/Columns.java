package com.tarento.analytics.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Columns {
	private Long id;
	private ColData data;
	private String visualizationCode;
	
	public String getVisualizationCode() {
		return visualizationCode;
	}
	public void setVisualizationCode(String visualizationCode) {
		this.visualizationCode = visualizationCode;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ColData getData() {
		return data;
	}
	public void setData(ColData data) {
		this.data = data;
	}
	
}
