package io.github.matwein.xmc.fe.ui.charts;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import io.github.matwein.xmc.fe.ui.TooltipBuilder;

public class ChartSymbolHoverNode extends StackPane {
	private static final int POINT_RADIUS = 10;
	
	private final Tooltip tooltip;

	public ChartSymbolHoverNode(String message) {
		setPrefSize(POINT_RADIUS, POINT_RADIUS);
		
		tooltip = TooltipBuilder.build(message);
		Tooltip.install(this, tooltip);
	}
	
	public Tooltip getTooltip() {
		return tooltip;
	}
}
