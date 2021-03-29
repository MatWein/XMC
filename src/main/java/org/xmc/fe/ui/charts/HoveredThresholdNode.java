package org.xmc.fe.ui.charts;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class HoveredThresholdNode extends StackPane {
	private static final int POINT_RADIUS = 5;
	
	private final Label label;
	
	public HoveredThresholdNode(String message) {
		setPrefSize(POINT_RADIUS, POINT_RADIUS);
		
		label = new Label(message);
		label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
		
		setOnMouseEntered(mouseEvent -> {
			getChildren().setAll(label);
			toFront();
		});
		
		setOnMouseExited(mouseEvent -> getChildren().clear());
	}
	
	public Label getLabel() {
		return label;
	}
}
