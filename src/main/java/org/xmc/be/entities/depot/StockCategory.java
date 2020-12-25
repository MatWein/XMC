package org.xmc.be.entities.depot;

import org.xmc.be.entities.PersistentObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = StockCategory.TABLE_NAME)
public class StockCategory extends PersistentObject {
	public static final String TABLE_NAME = "STOCK_CATEGORIES";
	
	@Column(name = "NAME", nullable = false, unique = true)
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
