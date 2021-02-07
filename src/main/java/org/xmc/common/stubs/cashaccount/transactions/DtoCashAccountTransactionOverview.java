package org.xmc.common.stubs.cashaccount.transactions;

import com.querydsl.core.annotations.QueryProjection;
import org.xmc.common.stubs.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DtoCashAccountTransactionOverview extends DtoCashAccountTransaction {
    private LocalDateTime creationDate;
    private Money valueWithCurrency;
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

    	super(id, categoryId, categoryName, categoryIcon, usage, description, valutaDate, value, reference, referenceIban, referenceBank, creditorIdentifier, mandate);
     
    	this.creationDate = creationDate;
        this.saldoBefore = new Money(saldoBefore, currency);
        this.saldoAfter = new Money(saldoAfter, currency);
        this.valueWithCurrency = new Money(value, currency);
    }
	
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	
	public Money getValueWithCurrency() {
		return valueWithCurrency;
	}
	
	public void setValueWithCurrency(Money valueWithCurrency) {
		this.valueWithCurrency = valueWithCurrency;
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
