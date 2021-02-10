package org.xmc.fe.ui.dashboard;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

public class DashboardPane extends ScrollPane {
	private static final int COLUMNS = 8;
	private static final int ROWS = 15;
	
	private final DtoDashboardTile[][] tilesData = new DtoDashboardTile[COLUMNS][ROWS];
	private final DashboardTile[][] tiles = new DashboardTile[COLUMNS][ROWS];
	private final GridPane gridPane = new GridPane();
	private final SimpleBooleanProperty editable = new SimpleBooleanProperty(false);
	private final DataFormat buttonFormat = new DataFormat("DashboardPaneDataFormat");
	
	private TitledPane dragAndDropPane;
	
	public DashboardPane() {
		this.setFitToWidth(true);
		
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.getStyleClass().add("dashboard-grid-pane");
		
		for (int i = 0; i < COLUMNS; i++) {
			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPercentWidth(100.0 / COLUMNS);
			
			gridPane.getColumnConstraints().add(columnConstraints);
		}
		
		for (int i = 0; i < ROWS; i++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPrefHeight(150.0);
			
			gridPane.getRowConstraints().add(rowConstraints);
		}
		
		for (int c = 0; c < COLUMNS; c++) {
			for (int r = 0; r < ROWS; r++) {
				DashboardTile dashboardTile = new DashboardTile();
				
				dashboardTile.setOnDragOver(event -> {
					if (isEditable()) {
						Dragboard db = event.getDragboard();
						if (db.hasContent(buttonFormat) && dragAndDropPane != null) {
							if (!dashboardTile.getStyleClass().contains("dahboard-tile-hover")) {
								dashboardTile.getStyleClass().add("dahboard-tile-hover");
							}
						}
					}
				});
				
				dashboardTile.setOnDragExited(event -> dashboardTile.getStyleClass().clear());
				
				tiles[c][r] = dashboardTile;
				gridPane.add(dashboardTile, c, r, 1, 1);
			}
		}
		
		this.setContent(gridPane);
	}
	
	public void addTile(DtoDashboardTile tile) {
		tilesData[tile.getColumnIndex()][tile.getRowIndex()] = tile;
		
		TitledPane titledPane = new TitledPane(tile.getTitle(), new Pane());
		titledPane.setCollapsible(false);
		titledPane.setMaxHeight(Double.MAX_VALUE);
		
		titledPane.setOnDragDetected(event -> {
			if (isEditable()) {
				Dragboard db = titledPane.startDragAndDrop(TransferMode.MOVE);
				db.setDragView(titledPane.snapshot(null, null));
				ClipboardContent cc = new ClipboardContent();
				cc.put(buttonFormat, " ");
				db.setContent(cc);
				
				titledPane.setMouseTransparent(true);
				dragAndDropPane = titledPane;
			}
		});
		
		gridPane.setOnDragOver(event -> {
			if (isEditable()) {
				Dragboard db = event.getDragboard();
				if (db.hasContent(buttonFormat) && dragAndDropPane != null) {
					event.acceptTransferModes(TransferMode.MOVE);
				}
			}
		});
		
		gridPane.setOnDragDropped(event -> {
			if (isEditable()) {
				Dragboard db = event.getDragboard();
				
				if (db.hasContent(buttonFormat)) {
					Node node = event.getPickResult().getIntersectedNode();
					
					if (node instanceof DashboardTile) {
						int oldX = GridPane.getColumnIndex(dragAndDropPane);
						int oldY = GridPane.getRowIndex(dragAndDropPane);
						int newX = GridPane.getColumnIndex(node);
						int newY = GridPane.getRowIndex(node);
						
						DtoDashboardTile dtoDashboardTile = tilesData[oldX][oldY];
						
						boolean[][] tileSpace = calculateTileSpace(tilesData);
						freeTileSpace(tileSpace, dtoDashboardTile);
						
						dtoDashboardTile.setColumnIndex(newX);
						dtoDashboardTile.setRowIndex(newY);
						
						if (calculateIsEnoughSpace(dtoDashboardTile, tileSpace)) {
							gridPane.getChildren().remove(dragAndDropPane);
							gridPane.add(dragAndDropPane, newX, newY);
							
							tilesData[newX][newY] = dtoDashboardTile;
							tilesData[oldX][oldY] = null;
						} else {
							dtoDashboardTile.setColumnIndex(oldX);
							dtoDashboardTile.setRowIndex(oldY);
						}
					}
					
					event.setDropCompleted(true);
					
					dragAndDropPane.setMouseTransparent(false);
					dragAndDropPane = null;
				}
			}
		});
		
		GridPane.setFillWidth(titledPane, true);
		GridPane.setFillHeight(titledPane, true);
		
		gridPane.getChildren().removeIf(node -> Integer.valueOf(tile.getRowIndex()).equals(GridPane.getRowIndex(node))
				&& Integer.valueOf(tile.getColumnIndex()).equals(GridPane.getColumnIndex(node))
				&& !(node instanceof DashboardTile));
		
		gridPane.add(titledPane, tile.getColumnIndex(), tile.getRowIndex(), tile.getColumnSpan(), tile.getRowSpan());
	}
	
	private void freeTileSpace(boolean[][] tileSpace, DtoDashboardTile dtoDashboardTile) {
		for (int c = dtoDashboardTile.getColumnIndex(); c < dtoDashboardTile.getColumnIndex() + dtoDashboardTile.getColumnSpan() && c < COLUMNS; c++) {
			for (int r = dtoDashboardTile.getRowIndex(); r < dtoDashboardTile.getRowIndex() + dtoDashboardTile.getRowSpan() && r < ROWS; r++) {
				tileSpace[c][r] = false;
			}
		}
	}
	
	public boolean addTileAtFreePosition(DtoDashboardTile tile) {
		boolean[][] tileSpace = calculateTileSpace(tilesData);
		
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
	
	private boolean[][] calculateTileSpace(DtoDashboardTile[][] tilesData) {
		boolean[][] result = new boolean[tilesData.length][tilesData[0].length];
		
		for (int c = 0; c < tilesData.length; c++) {
			for (int r = 0; r < tilesData[c].length; r++) {
				if (tilesData[c][r] == null) {
					continue;
				}
				
				for (int a = c; a < c + tilesData[c][r].getColumnSpan() && a < COLUMNS; a++) {
					for (int b = r; b < r + tilesData[c][r].getRowSpan() && b < ROWS; b++) {
						result[a][b] = true;
					}
				}
			}
		}
		
		return result;
	}
	
	private boolean addTileIfEnoughSpace(DtoDashboardTile tile, boolean[][] tileSpace) {
		boolean spaceFree = calculateIsEnoughSpace(tile, tileSpace);
		if (spaceFree) {
			addTile(tile);
			return true;
		}
		
		return false;
	}
	
	private boolean calculateIsEnoughSpace(DtoDashboardTile tile, boolean[][] tileSpace) {
		if (tile.getColumnIndex() + tile.getColumnSpan() > COLUMNS) {
			return false;
		}
		if (tile.getRowIndex() + tile.getRowSpan() > ROWS) {
			return false;
		}
		
		boolean spaceFree = true;
		for (int c = tile.getColumnIndex(); c < tile.getColumnIndex() + tile.getColumnSpan() && c < COLUMNS; c++) {
			for (int r = tile.getRowIndex(); r < tile.getRowIndex() + tile.getRowSpan() && r < ROWS; r++) {
				spaceFree &= !tileSpace[c][r];
			}
		}
		return spaceFree;
	}
	
	public boolean isEditable() {
		return editable.get();
	}
	
	public SimpleBooleanProperty editableProperty() {
		return editable;
	}
}
