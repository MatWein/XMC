package io.github.matwein.xmc.common.stubs.cashaccount.transactions;

import io.github.matwein.xmc.common.annotations.ExportField;
import io.github.matwein.xmc.common.stubs.Money;

import java.time.LocalDateTime;

public class DtoCashAccountTransactionOverview extends DtoCashAccountTransaction {
	@ExportField(index = 10, messageKey = "export.bean.cashaccount.transaction.overview.creationdate")
    private LocalDateTime creationDate;
	@ExportField(index = 30, messageKey = "export.bean.cashaccount.transaction.overview.valueWithCurrency")
    private Money valueWithCurrency;
	@ExportField(index = 40, messageKey = "export.bean.cashaccount.transaction.overview.saldoBefore")
    private Money saldoBefore;
	@ExportField(index = 50, messageKey = "export.bean.cashaccount.transaction.overview.saldoAfter")
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
