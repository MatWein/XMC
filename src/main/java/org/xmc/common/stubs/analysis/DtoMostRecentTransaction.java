package org.xmc.common.stubs.analysis;

import java.awt.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class DtoMostRecentTransaction implements Serializable {
	private BigDecimal value;
	private boolean positive;
	
	private LocalDate date;
	private Currency currency;
	private String description;
	
	private String assetName;
	private Color assetColor;
	
	public BigDecimal getValue() {
		return value;
	}
	
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public boolean isPositive() {
		return positive;
	}
	
	public void setPositive(boolean positive) {
		this.positive = positive;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public Currency getCurrency() {
		return currency;
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAssetName() {
		return assetName;
	}
	
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	
	public Color getAssetColor() {
		return assetColor;
	}
	
	public void setAssetColor(Color assetColor) {
		this.assetColor = assetColor;
	}
}
