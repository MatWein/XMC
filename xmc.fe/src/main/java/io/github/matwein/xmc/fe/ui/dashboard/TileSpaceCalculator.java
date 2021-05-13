package io.github.matwein.xmc.fe.ui.dashboard;

import static io.github.matwein.xmc.fe.ui.dashboard.DashboardPane.COLUMNS;
import static io.github.matwein.xmc.fe.ui.dashboard.DashboardPane.ROWS;

class TileSpaceCalculator {
	static boolean[][] calculateTileSpace(DtoDashboardTile[][] tilesData) {
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
	
	static boolean calculateIsEnoughSpace(DtoDashboardTile tile, boolean[][] tileSpace) {
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
	
	static void freeTileSpace(boolean[][] tileSpace, DtoDashboardTile dtoDashboardTile) {
		for (int c = dtoDashboardTile.getColumnIndex(); c < dtoDashboardTile.getColumnIndex() + dtoDashboardTile.getColumnSpan() && c < COLUMNS; c++) {
			for (int r = dtoDashboardTile.getRowIndex(); r < dtoDashboardTile.getRowIndex() + dtoDashboardTile.getRowSpan() && r < ROWS; r++) {
				tileSpace[c][r] = false;
			}
		}
	}
}
