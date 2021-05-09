package io.github.matwein.xmc.fe.ui.dashboard;

import com.google.gson.Gson;
import io.github.matwein.xmc.Main;
import io.github.matwein.xmc.common.utils.SleepUtil;
import io.github.matwein.xmc.fe.FeConstants;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.apache.commons.lang3.tuple.Pair;

public class DashboardPane extends ScrollPane {
	public static final String CSS_CLASS_DASHBOARD_GRID_PANE = "dashboard-grid-pane";
	
	static final DataFormat CLIP_BOARD_DATA_FORMAT = new DataFormat("DashboardPaneDataFormat");
	
	static final int COLUMNS = 8;
	static final int ROWS = 15;
	private static final double DEFAULT_TILE_HEIGHT = 150.0;
	
	private final GridPane gridPane = new GridPane();
	private final SimpleBooleanProperty editable = new SimpleBooleanProperty(false);
	
	private DtoDashboardTile[][] tilesData = new DtoDashboardTile[COLUMNS][ROWS];
	private DashboardContentTile dragAndDropTile;
	
	public DashboardPane() {
		this.setFitToWidth(true);
		this.getStylesheets().add(FeConstants.DASHBOARD_CSS_PATH);
		
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.getStyleClass().add(CSS_CLASS_DASHBOARD_GRID_PANE);
		
		for (int i = 0; i < COLUMNS; i++) {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPercentWidth(100.0 / COLUMNS);
			
			gridPane.getColumnConstraints().add(columnConstraints);
		}
		
		for (int i = 0; i < ROWS; i++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPrefHeight(DEFAULT_TILE_HEIGHT);
			
			gridPane.getRowConstraints().add(rowConstraints);
		}
		
		for (int c = 0; c < COLUMNS; c++) {
			for (int r = 0; r < ROWS; r++) {
				EmptyDashboardTile dashboardTile = new EmptyDashboardTile(this);
				gridPane.add(dashboardTile, c, r, 1, 1);
			}
		}
		
		gridPane.setOnDragOver(event -> {
			if (isEditable()) {
				Dragboard db = event.getDragboard();
				if (db.hasContent(CLIP_BOARD_DATA_FORMAT) && dragAndDropTile != null) {
					event.acceptTransferModes(TransferMode.MOVE);
				}
			}
		});
		
		gridPane.setOnDragDropped(event -> {
			if (isEditable()) {
				Dragboard db = event.getDragboard();
				
				if (db.hasContent(CLIP_BOARD_DATA_FORMAT)) {
					Node node = event.getPickResult().getIntersectedNode();
					
					if (node instanceof EmptyDashboardTile) {
						int oldX = GridPane.getColumnIndex(dragAndDropTile);
						int oldY = GridPane.getRowIndex(dragAndDropTile);
						int newX = GridPane.getColumnIndex(node);
						int newY = GridPane.getRowIndex(node);
						
						DtoDashboardTile dtoDashboardTile = tilesData[oldX][oldY];
						
						if (dtoDashboardTile != null) {
							boolean[][] tileSpace = TileSpaceCalculator.calculateTileSpace(tilesData);
							TileSpaceCalculator.freeTileSpace(tileSpace, dtoDashboardTile);
							
							dtoDashboardTile.setColumnIndex(newX);
							dtoDashboardTile.setRowIndex(newY);
							
							if (TileSpaceCalculator.calculateIsEnoughSpace(dtoDashboardTile, tileSpace)) {
								gridPane.getChildren().remove(dragAndDropTile);
								gridPane.add(dragAndDropTile, newX, newY);
								
								tilesData[newX][newY] = dtoDashboardTile;
								tilesData[oldX][oldY] = null;
							} else {
								dtoDashboardTile.setColumnIndex(oldX);
								dtoDashboardTile.setRowIndex(oldY);
							}
						}
					}
					
					event.setDropCompleted(true);
					
					dragAndDropTile.setMouseTransparent(false);
					dragAndDropTile = null;
				}
			}
		});
		
		this.setContent(gridPane);
	}
	
	public boolean addTileAtNextFreePosition(DtoDashboardTile tile) {
		boolean[][] tileSpace = TileSpaceCalculator.calculateTileSpace(tilesData);
		
		if (addTileIfEnoughSpace(tile, tileSpace)) return true;
		
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLUMNS; c++) {
				tile.setColumnIndex(c);
				tile.setRowIndex(r);
				
				if (addTileIfEnoughSpace(tile, tileSpace)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean addTileIfEnoughSpace(DtoDashboardTile tile, boolean[][] tileSpace) {
		boolean spaceFree = TileSpaceCalculator.calculateIsEnoughSpace(tile, tileSpace);
		if (spaceFree) {
			addTile(tile);
			return true;
		}
		
		return false;
	}
	
	private void addTile(DtoDashboardTile tile) {
		tilesData[tile.getColumnIndex()][tile.getRowIndex()] = tile;
		
		DashboardContentTile contentTile = new DashboardContentTile(this, tile);
		
		gridPane.getChildren().removeIf(node -> Integer.valueOf(tile.getRowIndex()).equals(GridPane.getRowIndex(node))
				&& Integer.valueOf(tile.getColumnIndex()).equals(GridPane.getColumnIndex(node))
				&& !(node instanceof EmptyDashboardTile));
		
		if (tile.getFxmlKey() == null) {
			return;
		}
		
		gridPane.add(contentTile, tile.getColumnIndex(), tile.getRowIndex(), tile.getColumnSpan(), tile.getRowSpan());
		
		Main.applicationContext.getBean(AsyncProcessor.class).runAsync(
				monitor -> {
					SleepUtil.sleep(2000);
					Pair<Parent, IDashboardTileController> fxmlComponent = FxmlComponentFactory.load(tile.getFxmlKey());
					
					boolean dataLoadingSuccessful = fxmlComponent.getRight().loadAndApplyData(monitor, tile, contentTile);
					if (dataLoadingSuccessful) {
						return fxmlComponent.getLeft();
					} else {
						return null;
					}
				},
				resultNode -> {
					if (resultNode == null) {
						removeTile(tile);
					} else {
						contentTile.setContentNode(resultNode);
					}
				}
		);
	}
	
	public void removeTile(DtoDashboardTile tile) {
		this.tilesData[tile.getColumnIndex()][tile.getRowIndex()] = null;
		gridPane.getChildren().removeIf(node -> node instanceof DashboardContentTile && ((DashboardContentTile) node).getTile() == tile);
	}
	
	public void applyTilesData(String tilesData) {
		gridPane.getChildren().removeIf(node -> node instanceof DashboardContentTile);
		
		if (tilesData == null) {
			return;
		}
		
		this.tilesData = new Gson().fromJson(tilesData, DtoDashboardTile[][].class);
		
		for (DtoDashboardTile[] tilesDatum : this.tilesData) {
			for (DtoDashboardTile tile : tilesDatum) {
				if (tile == null) {
					continue;
				}
				
				addTile(tile);
			}
		}
	}
	
	public String saveTilesData() {
		return new Gson().toJson(tilesData);
	}
	
	public boolean isEditable() {
		return editable.get();
	}
	
	public SimpleBooleanProperty editableProperty() {
		return editable;
	}
	
	DashboardContentTile getDragAndDropTile() {
		return dragAndDropTile;
	}
	
	void setDragAndDropTile(DashboardContentTile dragAndDropTile) {
		this.dragAndDropTile = dragAndDropTile;
	}
}
