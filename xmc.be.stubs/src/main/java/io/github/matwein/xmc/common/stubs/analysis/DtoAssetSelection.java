package io.github.matwein.xmc.common.stubs.analysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DtoAssetSelection implements Serializable {
	private Long id;
	private AssetType assetType;
	private String name;
	
	public DtoAssetSelection() {
	}
	
	public DtoAssetSelection(AssetType assetType, String name) {
		this.assetType = assetType;
		this.name = name;
	}
	
	private List<DtoAssetSelection> children = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public AssetType getAssetType() {
		return assetType;
	}
	
	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<DtoAssetSelection> getChildren() {
		return children;
	}
	
	public void setChildren(List<DtoAssetSelection> children) {
		this.children = children;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
