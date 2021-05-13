package io.github.matwein.xmc.common.stubs.depot.items;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

public class DtoDepotItem implements Serializable {
	private Long id;
	
	private String isin;
	private BigDecimal amount;
	private BigDecimal course;
	private BigDecimal value;
	private Currency currency;
	
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
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("isin", isin)
				.append("amount", amount)
				.append("course", course)
				.append("value", value)
				.append("currency", currency)
				.toString();
	}
}
