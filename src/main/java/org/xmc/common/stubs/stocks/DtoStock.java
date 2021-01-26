package org.xmc.common.stubs.stocks;

import org.xmc.common.stubs.category.DtoStockCategory;

import java.io.Serializable;

public class DtoStock implements Serializable {
	private Long id;
	private String isin;
	private String name;
	private String wkn;
	private DtoStockCategory stockCategory;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getIsin() {
		return isin;
	}
	
	public void setIsin(String isin) {
		this.isin = isin;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getWkn() {
		return wkn;
	}
	
	public void setWkn(String wkn) {
		this.wkn = wkn;
	}
	
	public DtoStockCategory getStockCategory() {
		return stockCategory;
	}
	
	public void setStockCategory(DtoStockCategory stockCategory) {
		this.stockCategory = stockCategory;
	}
}
