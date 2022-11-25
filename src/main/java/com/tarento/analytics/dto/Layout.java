package com.tarento.analytics.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Layout {
	
	private String layoutConfig;

	public String getLayoutConfig() {
		return layoutConfig;
	}

	public void setLayoutConfig(String layoutConfig) {
		this.layoutConfig = layoutConfig;
	}
	
}
