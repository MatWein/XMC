package org.xmc.fe.ui.dashboard;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;
import org.xmc.fe.ui.StageBuilder;

@Disabled
class DashboardPaneTest extends JUnitTestBase {
	private String tilesData;
	
	@Test
	void testBuild() throws Throwable {
		runJavaFxCode(() -> {
			DashboardPane dashboardPane = new DashboardPane();
			TextField columnSpanTextField = new TextField("1");
			TextField rowSpanTextField = new TextField("1");
			
			CheckBox editableCheckBox = new CheckBox();
			dashboardPane.editableProperty().bind(editableCheckBox.selectedProperty());
			
			HBox menuBar = new HBox();
			menuBar.getChildren().add(editableCheckBox);
			menuBar.getChildren().add(columnSpanTextField);
			menuBar.getChildren().add(rowSpanTextField);
			menuBar.getChildren().add(createButton("Neues Tile", e -> dashboardPane.addTileAtNextFreePosition(createTile(
					Integer.parseInt(columnSpanTextField.getText()) + " x " + Integer.parseInt(rowSpanTextField.getText()), 0, 0,
					Integer.parseInt(columnSpanTextField.getText()),
					Integer.parseInt(rowSpanTextField.getText())))));
			menuBar.getChildren().add(createButton("Speichern", e -> {
				tilesData = dashboardPane.saveTilesData();
			}));
			menuBar.getChildren().add(createButton("Laden", e -> dashboardPane.applyTilesData(tilesData)));
			
			VBox vBox = new VBox();
			vBox.getChildren().add(menuBar);
			vBox.getChildren().add(dashboardPane);
			vBox.setFillWidth(true);
			
			Stage result = StageBuilder.getInstance()
					.resizable(true)
					.withDefaultIcon()
					.withSceneComponent(vBox)
					.maximized(true)
					.minSize(800, 600)
					.withDefaultTitleKey()
					.build();
			
			result.getScene().getStylesheets().add("/css/xmc-base.css");
			
			result.showAndWait();
		});
	}
	
	private DtoDashboardTile createTile(String title, int columnIndex, int rowIndex, int columnSpan, int rowSpan) {
		var tile = new DtoDashboardTile();
		
		tile.setTitle(title);
		tile.setColumnIndex(columnIndex);
		tile.setRowIndex(rowIndex);
		tile.setColumnSpan(columnSpan);
		tile.setRowSpan(rowSpan);
		
		return tile;
	}
	
	private Button createButton(String text, EventHandler<ActionEvent> onAction) {
		Button button = new Button(text);
		button.setOnAction(onAction);
		return button;
	}
}