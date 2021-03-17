package org.xmc.common.stubs.depot.transactions;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class DtoDepotTransactionImportRow implements Serializable {
	private LocalDate valutaDate;
	private String isin;
	private BigDecimal amount;
	private BigDecimal course;
	private BigDecimal value;
	private String description;
	private Currency currency;
	
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
	
	public Currency getCurrency() {
		return currency;
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public LocalDate getValutaDate() {
		return valutaDate;
	}
	
	public void setValutaDate(LocalDate valutaDate) {
		this.valutaDate = valutaDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
