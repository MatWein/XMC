package org.xmc.common.stubs.analysis;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class DtoAssetDeliveries implements Serializable {
	private long assetId;
	private AssetType assetType;
	private String assetName;
	
	private List<Pair<LocalDateTime, Double>> deliveries = Lists.newArrayList();
	
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
	
	public List<Pair<LocalDateTime, Double>> getDeliveries() {
		return deliveries;
	}
	
	public void setDeliveries(List<Pair<LocalDateTime, Double>> deliveries) {
		this.deliveries = deliveries;
	}
	
	public AssetType getAssetType() {
		return assetType;
	}
	
	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}
}
