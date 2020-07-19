package org.xmc.common.stubs.cashaccount;

import org.xmc.common.stubs.DtoCategory;
import org.xmc.common.stubs.bank.DtoBank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DtoCashAccountTransactionOverview implements Serializable {
    private DtoBank referenceBank;
    private DtoCategory category;
    private String usage;
    private String description;
    private LocalDateTime valutaDate;
    private BigDecimal value;
    private String reference;
    private String creditorIdentifier;
    private String mandate;
    private BigDecimal saldoBefore;
    private BigDecimal saldoAfter;

    public DtoBank getReferenceBank() {
        return referenceBank;
    }

    public void setReferenceBank(DtoBank referenceBank) {
        this.referenceBank = referenceBank;
    }

    public DtoCategory getCategory() {
        return category;
    }

    public void setCategory(DtoCategory category) {
        this.category = category;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getValutaDate() {
        return valutaDate;
    }

    public void setValutaDate(LocalDateTime valutaDate) {
        this.valutaDate = valutaDate;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCreditorIdentifier() {
        return creditorIdentifier;
    }

    public void setCreditorIdentifier(String creditorIdentifier) {
        this.creditorIdentifier = creditorIdentifier;
    }

    public String getMandate() {
        return mandate;
    }

    public void setMandate(String mandate) {
        this.mandate = mandate;
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
