package org.xmc.common.stubs.cashaccount.transactions;

import com.querydsl.core.annotations.QueryProjection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DtoCashAccountTransactionOverview extends DtoCashAccountTransaction {
    private LocalDateTime creationDate;
    private BigDecimal saldoBefore;
    private BigDecimal saldoAfter;

    public DtoCashAccountTransactionOverview() {
    }

    @QueryProjection
    public DtoCashAccountTransactionOverview(
            Long id,
            Long categoryId,
            String categoryName,
            byte[] categoryIcon,
            String usage,
            String description,
            LocalDate valutaDate,
            BigDecimal value,
            String reference,
            String referenceIban,
            String referenceBank,
            String creditorIdentifier,
            String mandate,
            LocalDateTime creationDate,
            BigDecimal saldoBefore,
            BigDecimal saldoAfter) {

        super(id, categoryId, categoryName, categoryIcon, usage, description, valutaDate,
                value, reference, referenceIban, referenceBank, creditorIdentifier, mandate);

        this.creationDate = creationDate;
        this.saldoBefore = saldoBefore;
        this.saldoAfter = saldoAfter;
    }

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
