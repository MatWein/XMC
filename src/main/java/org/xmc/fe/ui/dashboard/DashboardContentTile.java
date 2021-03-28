package org.xmc.fe.ui.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.xmc.common.utils.ImageUtil;
import org.xmc.fe.FeConstants;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.ButtonImageView;

public class DashboardContentTile extends VBox {
	private static final String CSS_CLASS_DASHBOARD_CONTENT_TILE = "dashboard-content-tile";
	private static final String CSS_CLASS_TITLE_BAR = "title-bar";
	private static final String CSS_CLASS_BOLD = "bold";
	private static final String CSS_CLASS_TITLE_LABEL = "title-label";
	
	private final DtoDashboardTile tile;
	private Node contentNode;
	
	public DashboardContentTile(DashboardPane dashboardPane, DtoDashboardTile tile) {
		this.tile = tile;
		this.setMaxHeight(Double.MAX_VALUE);
		this.getStyleClass().add(CSS_CLASS_DASHBOARD_CONTENT_TILE);
		GridPane.setFillWidth(this, true);
		GridPane.setFillHeight(this, true);
		
		HBox titleBar = new HBox();
		titleBar.setAlignment(Pos.CENTER_LEFT);
		titleBar.getStyleClass().add(CSS_CLASS_TITLE_BAR);
		
		ImageView moveIconView = new ImageView(ImageUtil.invertColors(FeConstants.IMAGE_MOVE));
		moveIconView.visibleProperty().bind(dashboardPane.editableProperty());
		moveIconView.managedProperty().bind(moveIconView.visibleProperty());
		HBox.setMargin(moveIconView, new Insets(0, 0, 0, 5));
		titleBar.getChildren().add(moveIconView);
		
		Label titleLabel = new Label(tile.getTitle());
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
		removeTileButton.setTooltip(new Tooltip(MessageAdapter.getByKey(MessageKey.DASHBOARD_REMOVE_TILE)));
		removeTileButton.setGraphic(new ButtonImageView(FeConstants.IMAGE_MINUS_SQUARE));
		removeTileButton.visibleProperty().bind(dashboardPane.editableProperty());
		removeTileButton.setOnAction(e -> dashboardPane.removeTile(tile));
		titleBar.getChildren().add(removeTileButton);
		
		getChildren().add(titleBar);
		
		VBox spinnerPane = FxmlComponentFactory.loadWithoutController(FxmlKey.FRAGMENT_SPINNER_PANE);
		
		this.contentNode = spinnerPane;
		
		VBox.setVgrow(spinnerPane, Priority.ALWAYS);
		getChildren().add(spinnerPane);
		
		setCursor(dashboardPane.isEditable());
		
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
		
		dashboardPane.editableProperty().addListener((observable, oldValue, newValue) -> setCursor(newValue));
	}
	
	private void setCursor(Boolean editable) {
		if (Boolean.TRUE.equals(editable)) {
			setCursor(Cursor.MOVE);
		} else {
			setCursor(Cursor.DEFAULT);
		}
	}
	
	public Node getContentNode() {
		return contentNode;
	}
	
	public void setContentNode(Node contentNode) {
		getChildren().remove(this.contentNode);
		getChildren().add(contentNode);
		
		this.contentNode = contentNode;
	}
	
	public DtoDashboardTile getTile() {
		return tile;
	}
}