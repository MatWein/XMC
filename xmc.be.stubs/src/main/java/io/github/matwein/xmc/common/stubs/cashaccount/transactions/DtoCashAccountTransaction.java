package io.github.matwein.xmc.common.stubs.cashaccount.transactions;

import io.github.matwein.xmc.common.annotations.ExportField;
import io.github.matwein.xmc.common.stubs.category.DtoCategory;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DtoCashAccountTransaction implements Serializable {
    private Long id;
	private BigDecimal value;
	
	@ExportField(index = 60, messageKey = "export.bean.cashaccount.transaction.overview.category")
    private DtoCategory category;
	@ExportField(index = 70, messageKey = "export.bean.cashaccount.transaction.overview.usage")
    private String usage;
	@ExportField(index = 80, messageKey = "export.bean.cashaccount.transaction.overview.description")
    private String description;
	@ExportField(index = 20, messageKey = "export.bean.cashaccount.transaction.overview.valutaDate")
    private LocalDate valutaDate;
	@ExportField(index = 90, messageKey = "export.bean.cashaccount.transaction.overview.reference")
    private String reference;
	@ExportField(index = 100, messageKey = "export.bean.cashaccount.transaction.overview.referenceIban")
    private String referenceIban;
	@ExportField(index = 110, messageKey = "export.bean.cashaccount.transaction.overview.referenceBank")
    private String referenceBank;
	@ExportField(index = 120, messageKey = "export.bean.cashaccount.transaction.overview.creditorIdentifier")
    private String creditorIdentifier;
	@ExportField(index = 130, messageKey = "export.bean.cashaccount.transaction.overview.mandate")
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
