package org.xmc.common.stubs.cashaccount;

import com.querydsl.core.annotations.QueryProjection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DtoCashAccountOverview extends DtoCashAccount {
    private LocalDateTime creationDate;
    private BigDecimal lastSaldo;
    private LocalDateTime lastSaldoDate;

    public DtoCashAccountOverview() {
    }

    @QueryProjection
    public DtoCashAccountOverview(
            Long id, String iban, String number, String name, String currency,
            LocalDateTime creationDate, BigDecimal lastSaldo, LocalDateTime lastSaldoDate,
            Long bankId, String bankName, String bic, String blz, byte[] logo) {

        super(id, iban, number, name, currency, bankId, bankName, bic, blz, logo);

        this.creationDate = creationDate;
        this.lastSaldo = lastSaldo;
        this.lastSaldoDate = lastSaldoDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getLastSaldo() {
        return lastSaldo;
    }

    public void setLastSaldo(BigDecimal lastSaldo) {
        this.lastSaldo = lastSaldo;
    }

    public LocalDateTime getLastSaldoDate() {
        return lastSaldoDate;
    }

    public void setLastSaldoDate(LocalDateTime lastSaldoDate) {
        this.lastSaldoDate = lastSaldoDate;
    }
}
