package org.xmc.common.stubs;

import java.io.Serializable;
import java.math.BigDecimal;

public class Percentage implements Serializable {
    private BigDecimal fraction;

    public Percentage(BigDecimal fraction) {
        this.fraction = fraction == null ? new BigDecimal(0.0) : fraction;
    }

    public BigDecimal getValue() {
        return fraction.multiply(new BigDecimal(100));
    }

    public BigDecimal getFraction() {
        return fraction;
    }

    public void setFraction(BigDecimal fraction) {
        this.fraction = fraction;
    }
}
