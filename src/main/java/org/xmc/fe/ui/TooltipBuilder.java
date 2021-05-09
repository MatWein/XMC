package org.xmc.fe.ui;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class TooltipBuilder {
	public static Tooltip build(String message) {
		Tooltip tooltip = new Tooltip(message);
		
		tooltip.setShowDuration(Duration.INDEFINITE);
		tooltip.setShowDelay(Duration.millis(200.0));
		
		return tooltip;
	}
}
