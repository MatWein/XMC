package io.github.matwein.xmc.common.stubs.analysis;

import com.google.common.collect.Lists;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartPoint;

import java.io.Serializable;
import java.util.List;

public class DtoAssetPoints implements Serializable {
	private long assetId;
	private AssetType assetType;
	private String assetName;
	private String assetColor;
	
	private List<DtoChartPoint<Number, Number>> points = Lists.newArrayList();
	
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
	
	public List<DtoChartPoint<Number, Number>> getPoints() {
		return points;
	}
	
	public void setPoints(List<DtoChartPoint<Number, Number>> points) {
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
