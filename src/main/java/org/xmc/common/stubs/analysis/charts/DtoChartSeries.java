package org.xmc.common.stubs.analysis.charts;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class DtoChartSeries<X, Y> implements Serializable {
	private String name;
	
	private List<DtoChartPoint<X, Y>> points = Lists.newArrayList();
	
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
}
