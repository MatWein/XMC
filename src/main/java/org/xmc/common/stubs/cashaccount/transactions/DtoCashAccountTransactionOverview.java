package org.xmc.common.stubs.cashaccount.transactions;

import com.querydsl.core.annotations.QueryProjection;
import org.xmc.common.stubs.Money;
import org.xmc.common.stubs.category.DtoCategory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DtoCashAccountTransactionOverview implements Serializable {
    private Long id;

    private DtoCategory category;
    private String usage;
    private String description;
    private LocalDate valutaDate;
    private Money value;
    private String reference;
    private String referenceIban;
    private String referenceBank;
    private String creditorIdentifier;
    private String mandate;
    private LocalDateTime creationDate;
    private Money saldoBefore;
    private Money saldoAfter;

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
            BigDecimal saldoAfter,
            String currency) {

        this.id = id;

        if (categoryId != null) {
            this.category = new DtoCategory(categoryId, categoryName, categoryIcon);
        }

        this.usage = usage;
        this.description = description;
        this.valutaDate = valutaDate;
        this.value = new Money(value, currency);
        this.reference = reference;
        this.referenceIban = referenceIban;
        this.referenceBank = referenceBank;
        this.creditorIdentifier = creditorIdentifier;
        this.mandate = mandate;
        this.creationDate = creationDate;
        this.saldoBefore = new Money(saldoBefore, currency);
        this.saldoAfter = new Money(saldoAfter, currency);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getValutaDate() {
        return valutaDate;
    }

    public void setValutaDate(LocalDate valutaDate) {
        this.valutaDate = valutaDate;
    }

    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceIban() {
        return referenceIban;
    }

    public void setReferenceIban(String referenceIban) {
        this.referenceIban = referenceIban;
    }

    public String getReferenceBank() {
        return referenceBank;
    }

    public void setReferenceBank(String referenceBank) {
        this.referenceBank = referenceBank;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Money getSaldoBefore() {
        return saldoBefore;
    }

    public void setSaldoBefore(Money saldoBefore) {
        this.saldoBefore = saldoBefore;
    }

    public Money getSaldoAfter() {
        return saldoAfter;
    }

    public void setSaldoAfter(Money saldoAfter) {
        this.saldoAfter = saldoAfter;
    }
}
