package io.github.matwein.xmc.common.stubs.analysis.charts;

import java.io.Serializable;

public class DtoChartPoint<X, Y> implements Serializable {
	private X x;
	private Y y;
	private String description;
	
	public DtoChartPoint() {
	}
	
	public DtoChartPoint(X x, Y y) {
		this.x = x;
		this.y = y;
	}
	
	public DtoChartPoint(X x, Y y, String description) {
		this.x = x;
		this.y = y;
		this.description = description;
	}
	
	public X getX() {
		return x;
	}
	
	public void setX(X x) {
		this.x = x;
	}
	
	public Y getY() {
		return y;
	}
	
	public void setY(Y y) {
		this.y = y;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
