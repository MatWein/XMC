package io.github.matwein.xmc.common.stubs.analysis;

import java.io.Serializable;

public class DtoImportAnalyseFavouriteOverview implements Serializable {
	private long id;
	private String name;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
