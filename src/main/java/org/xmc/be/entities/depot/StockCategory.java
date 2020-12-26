package org.xmc.be.entities.depot;

import org.xmc.be.entities.DeletablePersistentObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = StockCategory.TABLE_NAME)
public class StockCategory extends DeletablePersistentObject {
	public static final String TABLE_NAME = "STOCK_CATEGORIES";
	
	@Column(name = "NAME", nullable = false)
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
