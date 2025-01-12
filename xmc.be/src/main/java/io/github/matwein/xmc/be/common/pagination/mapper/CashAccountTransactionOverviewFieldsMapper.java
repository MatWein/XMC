package io.github.matwein.xmc.be.common.pagination.mapper;

import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class CashAccountTransactionOverviewFieldsMapper implements IPagingFieldMapper<CashAccountTransactionOverviewFields> {
	@Override
	public String map(CashAccountTransactionOverviewFields pagingField) {
		return switch (pagingField) {
			case CATEGORY_NAME -> "c.name";
			case VALUTA_DATE -> "cat.valutaDate";
			case USAGE -> "cat.usage";
			case VALUE -> "cat.value";
			case DESCRIPTION -> "cat.description";
			case REFERENCE_BANK -> "cat.referenceBank";
			case REFERENCE_IBAN -> "cat.referenceIban";
			case REFERENCE -> "cat.reference";
			case CREDITOR_IDENTIFIER -> "cat.creditorIdentifier";
			case MANDATE -> "cat.mandate";
			case CREATION_DATE -> "cat.creationDate";
		};
	}
}
