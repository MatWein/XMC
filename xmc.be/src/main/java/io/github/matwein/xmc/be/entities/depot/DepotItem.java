package io.github.matwein.xmc.be.entities.depot;

import io.github.matwein.xmc.be.entities.DeletablePersistentObject;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = DepotItem.TABLE_NAME)
public class DepotItem extends DeletablePersistentObject {
	public static final String TABLE_NAME = "DEPOT_ITEMS";
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "DELIVERY_ID")
	private DepotDelivery delivery;
	
	@Column(name = "ISIN", nullable = false, length = 15)
	private String isin;
	
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
	
	public String getIsin() {
		return isin;
	}
	
	public void setIsin(String isin) {
		this.isin = isin;
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
