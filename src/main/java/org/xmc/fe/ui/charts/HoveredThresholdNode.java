package org.xmc.fe.ui.charts;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class HoveredThresholdNode extends StackPane {
	private static final int POINT_RADIUS = 5;
	
	public HoveredThresholdNode(String message) {
		setPrefSize(POINT_RADIUS, POINT_RADIUS);
		
		Label label = createDataThresholdLabel(message);
		
		setOnMouseEntered(mouseEvent -> {
			getChildren().setAll(label);
			toFront();
		});
		
		setOnMouseExited(mouseEvent -> getChildren().clear());
	}
	
	private Label createDataThresholdLabel(String message) {
		Label label = new Label(message);
		
		label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
		label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		
		return label;
	}
}
