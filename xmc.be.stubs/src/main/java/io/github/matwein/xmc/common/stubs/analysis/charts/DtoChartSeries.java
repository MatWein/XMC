package io.github.matwein.xmc.common.stubs.analysis.charts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoChartSeries<X, Y> implements Serializable {
	private String name;
	private String color;
	
	private List<DtoChartPoint<X, Y>> points = new ArrayList<>();
	private Map<String, Object> params = new HashMap<>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<DtoChartPoint<X, Y>> getPoints() {
		return points;
	}
	
	public void setPoints(List<DtoChartPoint<X, Y>> points) {
		this.points = points;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public Map<String, Object> getParams() {
		return params;
	}
	
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
