package com.tarento.analytics.dto;

import java.util.List;

public class DataSourceDto {
	
    private Long id;	
    private String title;
    private String srcType;
    private Long timestamp;
    private boolean isPinned;
    private List<DatasourceData> data;
    private String type;
    private Long createdBy;
    
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSrcType() {
		return srcType;
	}
	public void setSrcType(String srcType) {
		this.srcType = srcType;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public boolean getIsPinned() {
		return isPinned;
	}
	public void setIsPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}
	public List<DatasourceData> getData() {
		return data;
	}
	public void setData(List<DatasourceData> data) {
		this.data = data;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
