package com.tarento.analytics.dto;

import java.util.List;

import com.tarento.analytics.model.Columns;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LayoutDetail {
	private Long rowId;
	private List<Columns> cols;
	public Long getRowId() {
		return rowId;
	}
	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}
	public List<Columns> getCols() {
		return cols;
	}
	public void setCols(List<Columns> cols) {
		this.cols = cols;
	}
	
}
