package io.github.matwein.xmc.be.common.pagination.mapper;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.be.entities.cashaccount.QCashAccountTransaction;
import io.github.matwein.xmc.be.entities.cashaccount.QCategory;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class CashAccountTransactionOverviewFieldsMapper implements IPagingFieldMapper<CashAccountTransactionOverviewFields> {
	@Override
	public Expression<?> map(CashAccountTransactionOverviewFields pagingField) {
		return switch (pagingField) {
			case CATEGORY_NAME -> QCategory.category.name;
			case VALUTA_DATE -> QCashAccountTransaction.cashAccountTransaction.valutaDate;
			case USAGE -> QCashAccountTransaction.cashAccountTransaction.usage;
			case VALUE -> QCashAccountTransaction.cashAccountTransaction.value;
			case DESCRIPTION -> QCashAccountTransaction.cashAccountTransaction.description;
			case REFERENCE_BANK -> QCashAccountTransaction.cashAccountTransaction.referenceBank;
			case REFERENCE_IBAN -> QCashAccountTransaction.cashAccountTransaction.referenceIban;
			case REFERENCE -> QCashAccountTransaction.cashAccountTransaction.reference;
			case CREDITOR_IDENTIFIER -> QCashAccountTransaction.cashAccountTransaction.creditorIdentifier;
			case MANDATE -> QCashAccountTransaction.cashAccountTransaction.mandate;
			case CREATION_DATE -> QCashAccountTransaction.cashAccountTransaction.creationDate;
		};
	}
}
