package io.github.matwein.xmc.be.common.pagination.mapper;

import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class CashAccountOverviewFieldsMapper implements IPagingFieldMapper<CashAccountOverviewFields> {
	@Override
	public String map(CashAccountOverviewFields pagingField) {
		return switch (pagingField) {
			case BANK_NAME -> "b.name";
			case BANK_BIC -> "b.bic";
			case BANK_BLZ -> "b.blz";
			case NAME -> "ca.name";
			case IBAN -> "ca.iban";
			case NUMBER -> "ca.number";
			case CREATION_DATE -> "ca.creationDate";
			case CURRENCY -> "ca.currency";
			case LAST_SALDO -> "ca.lastSaldo";
			case LAST_SALDO_DATE -> "ca.lastSaldoDate";
		};
	}
}
