package io.github.matwein.xmc.be.entities.depot;

import io.github.matwein.xmc.be.entities.PersistentObject;
import jakarta.persistence.*;

@Entity
@Table(name = Stock.TABLE_NAME)
public class Stock extends PersistentObject {
	public static final String TABLE_NAME = "STOCKS";
	
	@Column(name = "ISIN", nullable = false, unique = true, length = 15)
	private String isin;
	@Column(name = "WKN", nullable = true, length = 10)
	private String wkn;
	@Column(name = "NAME", nullable = true)
	private String name;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "CATEGORY_ID")
	private StockCategory stockCategory;
	
	public String getIsin() {
		return isin;
	}
	
	public void setIsin(String isin) {
		this.isin = isin;
	}
	
	public String getWkn() {
		return wkn;
	}
	
	public void setWkn(String wkn) {
		this.wkn = wkn;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public StockCategory getStockCategory() {
		return stockCategory;
	}
	
	public void setStockCategory(StockCategory stockCategory) {
		this.stockCategory = stockCategory;
	}
}
