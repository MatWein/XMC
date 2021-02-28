package org.xmc.common.stubs.analysis;

public enum AnalysisType {
	ABSOLUTE_ASSET_VALUE (AssetType.CASHACCOUNT, AssetType.DEPOT),
	AGGREGATED_ASSET_VALUE (AssetType.CASHACCOUNT, AssetType.DEPOT),
	ABSOLUTE_AND_AGGREGATED_ASSET_VALUE (AssetType.CASHACCOUNT, AssetType.DEPOT)
	;
	
	private final AssetType[] assetTypesUsedForCalculation;
	
	AnalysisType(AssetType... assetTypesUsedForCalculation) {
		this.assetTypesUsedForCalculation = assetTypesUsedForCalculation;
	}
	
	public AssetType[] getAssetTypesUsedForCalculation() {
		return assetTypesUsedForCalculation;
	}
}
