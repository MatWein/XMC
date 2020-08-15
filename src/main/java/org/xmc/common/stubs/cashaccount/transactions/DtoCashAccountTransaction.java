package org.xmc.common.stubs.cashaccount.transactions;

import org.xmc.common.stubs.category.DtoCategory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DtoCashAccountTransaction implements Serializable {
    private Long id;
    private DtoCategory category;
    private String usage;
    private String description;
    private LocalDateTime valutaDate;
    private BigDecimal value;
    private String reference;
    private String referenceIban;
    private String referenceBank;
    private String creditorIdentifier;
    private String mandate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceBank() {
        return referenceBank;
    }

    public void setReferenceBank(String referenceBank) {
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

    public String getReferenceIban() {
        return referenceIban;
    }

    public void setReferenceIban(String referenceIban) {
        this.referenceIban = referenceIban;
    }
}
