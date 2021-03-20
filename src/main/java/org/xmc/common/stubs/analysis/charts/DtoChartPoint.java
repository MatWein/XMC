package org.xmc.common.stubs.analysis.charts;

import java.io.Serializable;

public class DtoChartPoint<X, Y> implements Serializable {
	private X x;
	private Y y;
	
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
}
