package io.github.matwein.xmc.fe.ui.charts;

import javafx.scene.chart.Axis;

public interface IChartBase<X, Y> {
	Axis<X> getXAxis();
	Axis<Y> getYAxis();
	
	boolean isShowHoverLabel();
	int getMaxHoverNodes();
}
