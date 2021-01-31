package org.xmc.common.stubs.depot.deliveries;

import com.querydsl.core.annotations.QueryProjection;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

public class DtoDepotItem implements Serializable {
	private Long id;
	
	private String isin;
	private BigDecimal amount;
	private BigDecimal course;
	private BigDecimal value;
	private String currency;
	
	public DtoDepotItem() {
	}
	
	@QueryProjection
	public DtoDepotItem(Long id, String isin, BigDecimal amount, BigDecimal course, BigDecimal value, String currency) {
		this.id = id;
		this.isin = isin;
		this.amount = amount;
		this.course = course;
		this.value = value;
		this.currency = currency;
	}
	
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
				.append("amount", amount)
				.append("course", course)
				.append("value", value)
				.append("currency", currency)
				.toString();
	}
}
