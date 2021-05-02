package org.xmc.common.stubs.analysis;

import java.io.Serializable;

public class DtoAnalysisDashboardTileData implements Serializable {
	private long analysisFavouriteId;
	private int tileWidth;
	private int tileHeight;
	
	public long getAnalysisFavouriteId() {
		return analysisFavouriteId;
	}
	
	public void setAnalysisFavouriteId(long analysisFavouriteId) {
		this.analysisFavouriteId = analysisFavouriteId;
	}
	
	public int getTileWidth() {
		return tileWidth;
	}
	
	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}
	
	public int getTileHeight() {
		return tileHeight;
	}
	
	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}
}
