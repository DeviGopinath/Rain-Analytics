package com.tarento.analytics.dto;

import java.util.List;

public class VisualizationsDto {	

	    private String title;
	    private boolean isPinned;
	    private boolean isDecimal;
	    private List<VisualizationData> data;
	    private String type;
	    private Long id;
		private String visualizationCode;
		private Long createdBy; 
		private Long timestamp;
		
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
		public void setDecimal(boolean isDecimal) {
			this.isDecimal = isDecimal;
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
		public boolean getIsDecimal() {
			return isDecimal;
		}
		public void setIsDecimal(boolean isDecimal) {
			this.isDecimal = isDecimal;
		}
		public List<VisualizationData> getData() {
			return data;
		}
		public void setData(List<VisualizationData> data) {
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
		public String getVisualizationCode() {
			return visualizationCode;
		}
		public void setVisualizationCode(String visualizationCode) {
			this.visualizationCode = visualizationCode;
		}
		
		
}
