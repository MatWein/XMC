package org.xmc.fe.ui.charts;

import javafx.scene.chart.Axis;

public interface IChartBase<X, Y> {
	Axis<X> getXAxis();
	Axis<Y> getYAxis();
	
	boolean isShowHoverLabel();
}
