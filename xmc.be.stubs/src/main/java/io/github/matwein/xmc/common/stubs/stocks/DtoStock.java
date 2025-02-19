package io.github.matwein.xmc.common.stubs.stocks;

import io.github.matwein.xmc.common.stubs.category.DtoStockCategory;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class DtoStock implements Serializable {
	private Long id;
	private String isin;
	private String name;
	private String wkn;
	private DtoStockCategory stockCategory;
	
	public DtoStock() {
	}
	
	public DtoStock(Long id, String isin, String name, String wkn, Long categoryId, String categoryName) {
		this.id = id;
		this.isin = isin;
		this.name = name;
		this.wkn = wkn;
		this.stockCategory = (categoryId != null ? new DtoStockCategory(categoryId, categoryName) : null);
	}
	
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
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("isin", isin)
				.append("name", name)
				.append("wkn", wkn)
				.append("stockCategory", stockCategory)
				.toString();
	}
}
