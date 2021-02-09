package org.xmc.fe.ui.dashboard;

import java.io.Serializable;

public class DtoDashboardTile implements Serializable {
	private String title;
	private int columnIndex;
	private int rowIndex;
	private int columnSpan;
	private int rowSpan;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getColumnIndex() {
		return columnIndex;
	}
	
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	
	public int getRowIndex() {
		return rowIndex;
	}
	
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	
	public int getColumnSpan() {
		return columnSpan;
	}
	
	public void setColumnSpan(int columnSpan) {
		this.columnSpan = columnSpan;
	}
	
	public int getRowSpan() {
		return rowSpan;
	}
	
	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}
}
