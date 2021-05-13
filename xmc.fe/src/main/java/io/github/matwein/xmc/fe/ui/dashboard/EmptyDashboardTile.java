package io.github.matwein.xmc.fe.ui.dashboard;

import javafx.scene.layout.StackPane;

public class EmptyDashboardTile extends StackPane {
	public static final String CSS_CLASS_DAHBOARD_TILE_HOVER = "dashboard-tile-hover";
	
	public EmptyDashboardTile(DashboardPane dashboardPane) {
		this.setOnDragOver(event -> {
			if (!dashboardPane.isEditable()) {
				return;
			}
			
			if (event.getDragboard().hasContent(DashboardPane.CLIP_BOARD_DATA_FORMAT)
					&& dashboardPane.getDragAndDropTile() != null
					&& !getStyleClass().contains(CSS_CLASS_DAHBOARD_TILE_HOVER)) {
			
				getStyleClass().add(CSS_CLASS_DAHBOARD_TILE_HOVER);
			}
		});
		
		this.setOnDragExited(event -> getStyleClass().clear());
	}
}
