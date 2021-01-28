package org.xmc.common.stubs.ccf;

import com.querydsl.core.annotations.QueryProjection;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public class DtoCurrencyConversionFactor implements Serializable {
	private Long id;
	
	private LocalDateTime inputDate;
	private Currency currency;
	private BigDecimal factorToEur;
	
	public DtoCurrencyConversionFactor() {
	}
	
	@QueryProjection
	public DtoCurrencyConversionFactor(Long id, LocalDateTime inputDate, String currency, BigDecimal factorToEur) {
		this.id = id;
		this.inputDate = inputDate;
		this.currency = Currency.getInstance(currency);
		this.factorToEur = factorToEur;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public LocalDateTime getInputDate() {
		return inputDate;
	}
	
	public void setInputDate(LocalDateTime inputDate) {
		this.inputDate = inputDate;
	}
	
	public Currency getCurrency() {
		return currency;
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public BigDecimal getFactorToEur() {
		return factorToEur;
	}
	
	public void setFactorToEur(BigDecimal factorToEur) {
		this.factorToEur = factorToEur;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("inputDate", inputDate)
				.append("currency", currency)
				.append("factorToEur", factorToEur)
				.toString();
	}
}
