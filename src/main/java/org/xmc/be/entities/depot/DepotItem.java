package org.xmc.be.entities.depot;

import org.xmc.be.entities.PersistentObject;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = DepotItem.TABLE_NAME)
public class DepotItem extends PersistentObject {
	public static final String TABLE_NAME = "DEPOT_ITEMS";
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "DELIVERY_ID")
	private DepotDelivery delivery;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "STOCK_ID")
	private Stock stock;
	
	@Column(name = "AMOUNT", nullable = false)
	private BigDecimal amount;
	
	@Column(name = "COURSE", nullable = false)
	private BigDecimal course;
	
	@Column(name = "VALUE", nullable = false)
	private BigDecimal value;
	
	@Column(name = "CURRENCY", nullable = false, length = 5)
	private String currency;
	
	public DepotDelivery getDelivery() {
		return delivery;
	}
	
	public void setDelivery(DepotDelivery delivery) {
		this.delivery = delivery;
	}
	
	public Stock getStock() {
		return stock;
	}
	
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getCourse() {
		return course;
	}
	
	public void setCourse(BigDecimal course) {
		this.course = course;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
