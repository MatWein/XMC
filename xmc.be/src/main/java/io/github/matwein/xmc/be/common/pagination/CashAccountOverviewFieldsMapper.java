package io.github.matwein.xmc.be.common.pagination;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.entities.QBank;
import io.github.matwein.xmc.be.entities.cashaccount.QCashAccount;
import io.github.matwein.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CashAccountOverviewFieldsMapper implements IPagingFieldMapper<CashAccountOverviewFields> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountOverviewFieldsMapper.class);
	
	@Override
	public Expression<?> map(CashAccountOverviewFields pagingField) {
		switch (pagingField) {
			case BANK_NAME:
				return QBank.bank.name;
			case BANK_BIC:
				return QBank.bank.bic;
			case BANK_BLZ:
				return QBank.bank.blz;
			case NAME:
				return QCashAccount.cashAccount.name;
			case IBAN:
				return QCashAccount.cashAccount.iban;
			case NUMBER:
				return QCashAccount.cashAccount.number;
			case CREATION_DATE:
				return QCashAccount.cashAccount.creationDate;
			case CURRENCY:
				return QCashAccount.cashAccount.currency;
			case LAST_SALDO:
				return QCashAccount.cashAccount.lastSaldo;
			case LAST_SALDO_DATE:
				return QCashAccount.cashAccount.lastSaldoDate;
			default:
				String message = String.format("Could not map enum value '%s' of type '%s' to expression.", pagingField, pagingField.getClass().getSimpleName());
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
