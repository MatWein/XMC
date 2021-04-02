package org.xmc.fe.ui.charts;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ChartSymbolHoverNode extends StackPane {
	private static final int POINT_RADIUS = 10;
	
	private final Tooltip tooltip;

	public ChartSymbolHoverNode(String message, String color) {
		setPrefSize(POINT_RADIUS, POINT_RADIUS);
		setStyle("-fx-bar-fill: " + color);
		
		tooltip = new Tooltip(message);
		tooltip.setShowDuration(Duration.INDEFINITE);
		tooltip.setShowDelay(Duration.millis(200.0));
		tooltip.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
		
		Tooltip.install(this, tooltip);
	}
	
	public Tooltip getTooltip() {
		return tooltip;
	}
}
