package org.xmc.fe.ui.dashboard;

import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;

import java.io.Serializable;

public class DtoDashboardTile implements Serializable {
	private String title;
	private FxmlKey fxmlKey;
	private Serializable data;
	
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
	
	public Serializable getData() {
		return data;
	}
	
	public void setData(Serializable data) {
		this.data = data;
	}
	
	public FxmlKey getFxmlKey() {
		return fxmlKey;
	}
	
	public void setFxmlKey(FxmlKey fxmlKey) {
		this.fxmlKey = fxmlKey;
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
