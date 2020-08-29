package org.xmc.common.stubs;

import java.io.Serializable;
import java.math.BigDecimal;

public class Money implements Serializable {
    private BigDecimal value;
    private String currency;

    public Money(BigDecimal value, String currency) {
        this.value = value == null ? new BigDecimal(0.0) : value;
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
