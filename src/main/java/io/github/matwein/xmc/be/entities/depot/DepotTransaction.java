package io.github.matwein.xmc.be.entities.depot;

import io.github.matwein.xmc.be.entities.DeletablePersistentObject;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = DepotTransaction.TABLE_NAME)
public class DepotTransaction extends DeletablePersistentObject {
	public static final String TABLE_NAME = "DEPOT_TRANSACTIONS";
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "DEPOT_ID")
	private Depot depot;
	
	@Column(name = "ISIN", nullable = false, length = 15)
	private String isin;
	
	@Column(name = "VALUTA_DATE", nullable = false)
	private LocalDate valutaDate;
	
	@Column(name = "AMOUNT", nullable = false)
	private BigDecimal amount;
	
	@Column(name = "COURSE", nullable = false)
	private BigDecimal course;
	
	@Column(name = "VALUE", nullable = false)
	private BigDecimal value;
	
	@Column(name = "DESCRIPTION", nullable = true, length = 1000)
	private String description;
	
	@Column(name = "CURRENCY", nullable = false, length = 5)
	private String currency;
	
	public Depot getDepot() {
		return depot;
	}
	
	public void setDepot(Depot depot) {
		this.depot = depot;
	}
	
	public String getIsin() {
		return isin;
	}
	
	public void setIsin(String isin) {
		this.isin = isin;
	}
	
	public LocalDate getValutaDate() {
		return valutaDate;
	}
	
	public void setValutaDate(LocalDate valutaDate) {
		this.valutaDate = valutaDate;
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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
