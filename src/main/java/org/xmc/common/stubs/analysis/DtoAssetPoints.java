package org.xmc.common.stubs.analysis;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.List;

public class DtoAssetPoints implements Serializable {
	private long assetId;
	private AssetType assetType;
	private String assetName;
	private String assetColor;
	
	private List<Pair<Number, Number>> points = Lists.newArrayList();
	
	public long getAssetId() {
		return assetId;
	}
	
	public void setAssetId(long assetId) {
		this.assetId = assetId;
	}
	
	public String getAssetName() {
		return assetName;
	}
	
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	
	public List<Pair<Number, Number>> getPoints() {
		return points;
	}
	
	public void setPoints(List<Pair<Number, Number>> points) {
		this.points = points;
	}
	
	public AssetType getAssetType() {
		return assetType;
	}
	
	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}
	
	public String getAssetColor() {
		return assetColor;
	}
	
	public void setAssetColor(String assetColor) {
		this.assetColor = assetColor;
	}
}
