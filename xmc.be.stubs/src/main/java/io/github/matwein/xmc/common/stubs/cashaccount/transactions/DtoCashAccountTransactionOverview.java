package io.github.matwein.xmc.common.stubs.cashaccount.transactions;

import io.github.matwein.xmc.common.stubs.Money;

import java.time.LocalDateTime;

public class DtoCashAccountTransactionOverview extends DtoCashAccountTransaction {
    private LocalDateTime creationDate;
    private Money valueWithCurrency;
    private Money saldoBefore;
    private Money saldoAfter;

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
