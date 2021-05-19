package io.github.matwein.xmc.common.stubs.depot.transactions;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DtoDepotTransaction implements Serializable {
	private Long id;
	
	private String isin;
	private LocalDate valutaDate;
	private BigDecimal amount;
	private BigDecimal course;
	private BigDecimal value;
	private String description;
	private String currency;
	
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
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("isin", isin)
				.append("valutaDate", valutaDate)
				.append("amount", amount)
				.append("course", course)
				.append("value", value)
				.append("description", description)
				.append("currency", currency)
				.toString();
	}
}
