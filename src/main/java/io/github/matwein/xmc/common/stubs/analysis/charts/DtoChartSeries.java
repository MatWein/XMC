package io.github.matwein.xmc.common.stubs.analysis.charts;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DtoChartSeries<X, Y> implements Serializable {
	private String name;
	private String color;
	
	private List<DtoChartPoint<X, Y>> points = Lists.newArrayList();
	private Map<String, Object> params = Maps.newHashMap();
	
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
