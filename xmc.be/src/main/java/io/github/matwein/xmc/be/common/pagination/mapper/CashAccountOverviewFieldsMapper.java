package io.github.matwein.xmc.be.common.pagination.mapper;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.be.entities.QBank;
import io.github.matwein.xmc.be.entities.cashaccount.QCashAccount;
import io.github.matwein.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class CashAccountOverviewFieldsMapper implements IPagingFieldMapper<CashAccountOverviewFields> {
	@Override
	public Expression<?> map(CashAccountOverviewFields pagingField) {
		return switch (pagingField) {
			case BANK_NAME -> QBank.bank.name;
			case BANK_BIC -> QBank.bank.bic;
			case BANK_BLZ -> QBank.bank.blz;
			case NAME -> QCashAccount.cashAccount.name;
			case IBAN -> QCashAccount.cashAccount.iban;
			case NUMBER -> QCashAccount.cashAccount.number;
			case CREATION_DATE -> QCashAccount.cashAccount.creationDate;
			case CURRENCY -> QCashAccount.cashAccount.currency;
			case LAST_SALDO -> QCashAccount.cashAccount.lastSaldo;
			case LAST_SALDO_DATE -> QCashAccount.cashAccount.lastSaldoDate;
		};
	}
}
