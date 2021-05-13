package io.github.matwein.xmc.common.stubs.cashaccount.transactions;

import com.querydsl.core.annotations.QueryProjection;
import io.github.matwein.xmc.common.stubs.category.DtoCategory;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DtoCashAccountTransaction implements Serializable {
    private Long id;

    private DtoCategory category;
    private String usage;
    private String description;
    private LocalDate valutaDate;
    private BigDecimal value;
    private String reference;
    private String referenceIban;
    private String referenceBank;
    private String creditorIdentifier;
    private String mandate;

    public DtoCashAccountTransaction() {
    }

    @QueryProjection
    public DtoCashAccountTransaction(
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
            String mandate) {

        this.id = id;

        if (categoryId != null) {
            this.category = new DtoCategory(categoryId, categoryName, categoryIcon);
        }

        this.usage = usage;
        this.description = description;
        this.valutaDate = valutaDate;
        this.value = value;
        this.reference = reference;
        this.referenceIban = referenceIban;
        this.referenceBank = referenceBank;
        this.creditorIdentifier = creditorIdentifier;
        this.mandate = mandate;
    }

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

    public String getReferenceIban() {
        return referenceIban;
    }

    public void setReferenceIban(String referenceIban) {
        this.referenceIban = referenceIban;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("category", category)
                .append("usage", usage)
                .append("description", description)
                .append("valutaDate", valutaDate)
                .append("value", value)
                .append("reference", reference)
                .append("referenceIban", referenceIban)
                .append("referenceBank", referenceBank)
                .append("creditorIdentifier", creditorIdentifier)
                .append("mandate", mandate)
                .toString();
    }
}
