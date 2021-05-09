package io.github.matwein.xmc.be.entities.depot;

import io.github.matwein.xmc.be.entities.PersistentObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = CurrencyConversionFactor.TABLE_NAME)
public class CurrencyConversionFactor extends PersistentObject {
	public static final String TABLE_NAME = "CURRENCY_CONVERSION_FACTORS";
	
	@Column(name = "INPUT_DATE", nullable = false)
	private LocalDateTime inputDate;
	
	@Column(name = "CURRENCY", nullable = false, length = 5)
	private String currency;
	
	@Column(name = "FACTOR_TO_EUR", nullable = false)
	private BigDecimal factorToEur;
	
	public LocalDateTime getInputDate() {
		return inputDate;
	}
	
	public void setInputDate(LocalDateTime inputDate) {
		this.inputDate = inputDate;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public BigDecimal getFactorToEur() {
		return factorToEur;
	}
	
	public void setFactorToEur(BigDecimal factorToEur) {
		this.factorToEur = factorToEur;
	}
}
