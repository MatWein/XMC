package org.xmc.fe.ui.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.xmc.common.utils.ImageUtil;
import org.xmc.fe.ui.components.ButtonImageView;

public class DashboardContentTile extends VBox {
	private static final String CSS_CLASS_DASHBOARD_CONTENT_TILE = "dashboard-content-tile";
	private static final String CSS_CLASS_TITLE_BAR = "title-bar";
	public static final String CSS_CLASS_BOLD = "bold";
	public static final String CSS_CLASS_TITLE_LABEL = "title-label";
	
	private Node contentNode;
	
	public DashboardContentTile(DashboardPane dashboardPane, String title) {
		this.setMaxHeight(Double.MAX_VALUE);
		this.getStyleClass().add(CSS_CLASS_DASHBOARD_CONTENT_TILE);
		
		HBox titleBar = new HBox();
		titleBar.setAlignment(Pos.CENTER_LEFT);
		titleBar.getStyleClass().add(CSS_CLASS_TITLE_BAR);
		
		ImageView moveIconView = new ImageView(ImageUtil.invertColors(new Image("/images/feather/move.png")));
		moveIconView.visibleProperty().bind(dashboardPane.editableProperty());
		moveIconView.managedProperty().bind(moveIconView.visibleProperty());
		HBox.setMargin(moveIconView, new Insets(0, 0, 0, 5));
		titleBar.getChildren().add(moveIconView);
		
		Label titleLabel = new Label(title);
		titleLabel.setTextAlignment(TextAlignment.LEFT);
		titleLabel.setAlignment(Pos.CENTER_LEFT);
		titleLabel.getStyleClass().add(CSS_CLASS_BOLD);
		titleLabel.getStyleClass().add(CSS_CLASS_TITLE_LABEL);
		titleLabel.setMaxWidth(Double.MAX_VALUE);
		titleLabel.setMaxHeight(Double.MAX_VALUE);
		HBox.setHgrow(titleLabel, Priority.ALWAYS);
		HBox.setMargin(titleLabel, new Insets(0, 0, 0, 5));
		titleBar.getChildren().add(titleLabel);
		
		Button removeTileButton = new Button();
		removeTileButton.setTooltip(new Tooltip("Kachel entfernen"));
		removeTileButton.setGraphic(new ButtonImageView("/images/feather/minus-square.png"));
		removeTileButton.visibleProperty().bind(dashboardPane.editableProperty());
		titleBar.getChildren().add(removeTileButton);
		
		getChildren().add(titleBar);
		
		VBox content = new VBox();
		content.setSpacing(10.0);
		content.setPadding(new Insets(0.0, 20.0, 0.0, 20.0));
		content.setAlignment(Pos.CENTER);
		content.setMaxWidth(Double.MAX_VALUE);
		content.setMaxHeight(Double.MAX_VALUE);
		
		ProgressBar spinner = new ProgressBar(-1.0);
		spinner.setMaxWidth(Double.MAX_VALUE);
		content.getChildren().add(new Label("Wird geladen..."));
		content.getChildren().add(spinner);
		
		this.contentNode = content;
		
		VBox.setVgrow(content, Priority.ALWAYS);
		getChildren().add(content);
		
		this.setOnDragDetected(event -> {
			if (dashboardPane.isEditable()) {
				Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
				db.setDragView(this.snapshot(null, null));
				ClipboardContent cc = new ClipboardContent();
				cc.put(DashboardPane.CLIP_BOARD_DATA_FORMAT, " ");
				db.setContent(cc);
				
				this.setMouseTransparent(true);
				dashboardPane.setDragAndDropTile(this);
			}
		});
		
		dashboardPane.editableProperty().addListener((observable, oldValue, newValue) -> {
			if (Boolean.TRUE.equals(newValue)) {
				setCursor(Cursor.MOVE);
			} else {
				setCursor(Cursor.DEFAULT);
			}
		});
	}
}
