package io.github.matwein.xmc.common.stubs;

import java.io.Serializable;
import java.math.BigDecimal;

public class Money implements Serializable {
	private static final String DEFAULT_CURRENCY = "EUR";
	
	private BigDecimal value;
    private String currency;
	
	public Money() {
		this.value = BigDecimal.ZERO;
		this.currency = DEFAULT_CURRENCY;
	}
	
	public Money(BigDecimal value) {
		this(value, DEFAULT_CURRENCY);
	}
	
	public Money(BigDecimal value, String currency) {
        this.value = value == null ? BigDecimal.valueOf(0.0) : value;
        this.currency = currency;
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
