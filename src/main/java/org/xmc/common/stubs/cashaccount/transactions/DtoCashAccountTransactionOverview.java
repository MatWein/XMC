package org.xmc.common.stubs.cashaccount.transactions;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DtoCashAccountTransactionOverview extends DtoCashAccountTransaction {
    private LocalDateTime creationDate;
    private BigDecimal saldoBefore;
    private BigDecimal saldoAfter;

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getSaldoBefore() {
        return saldoBefore;
    }

    public void setSaldoBefore(BigDecimal saldoBefore) {
        this.saldoBefore = saldoBefore;
    }

    public BigDecimal getSaldoAfter() {
        return saldoAfter;
    }

    public void setSaldoAfter(BigDecimal saldoAfter) {
        this.saldoAfter = saldoAfter;
    }
}
