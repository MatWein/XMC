package org.xmc.be.entities.cashaccount;

import org.xmc.be.entities.Category;
import org.xmc.be.entities.DeletablePersistentObject;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = CashAccountTransaction.TABLE_NAME)
public class CashAccountTransaction extends DeletablePersistentObject {
    public static final String TABLE_NAME = "CASH_ACCOUNT_TRANSACTIONS";

    @ManyToOne(optional = false)
    @JoinColumn(name = "CASH_ACCOUNT_ID")
    private CashAccount cashAccount;

    @ManyToOne(optional = true)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @Column(name = "USAGE", nullable = false, length = 1000)
    private String usage;

    @Column(name = "DESCRIPTION", nullable = true, length = 1000)
    private String description;

    @Column(name = "VALUTA_DATE", nullable = false)
    private LocalDate valutaDate;

    @Column(name = "VALUE", nullable = false)
    private BigDecimal value;

    @Column(name = "REFERENCE_BANK", nullable = true)
    private String referenceBank;

    @Column(name = "REFERENCE", nullable = true)
    private String reference;

    @Column(name = "REFERENCE_IBAN", nullable = true, length = 50)
    private String referenceIban;

    @Column(name = "CREDITOR_IDENTIFIER", nullable = true)
    private String creditorIdentifier;

    @Column(name = "MANDATE", nullable = true)
    private String mandate;

    @Column(name = "SALDO_BEFORE", nullable = false)
    private BigDecimal saldoBefore;

    @Column(name = "SALDO_AFTER", nullable = false)
    private BigDecimal saldoAfter;

    public CashAccount getCashAccount() {
        return cashAccount;
    }

    public void setCashAccount(CashAccount cashAccount) {
        this.cashAccount = cashAccount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
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

    public String getReferenceBank() {
        return referenceBank;
    }

    public void setReferenceBank(String referenceBank) {
        this.referenceBank = referenceBank;
    }

    public String getReferenceIban() {
        return referenceIban;
    }

    public void setReferenceIban(String referenceIban) {
        this.referenceIban = referenceIban;
    }
}
