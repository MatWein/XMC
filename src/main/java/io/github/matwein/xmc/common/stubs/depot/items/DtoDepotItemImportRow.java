package io.github.matwein.xmc.common.stubs.depot.items;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

public class DtoDepotItemImportRow implements Serializable {
	private String isin;
	private BigDecimal amount;
	private BigDecimal course;
	private BigDecimal value;
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
}
