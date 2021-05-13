package io.github.matwein.xmc.be.common.pagination;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.entities.cashaccount.QCashAccountTransaction;
import io.github.matwein.xmc.be.entities.cashaccount.QCategory;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CashAccountTransactionOverviewFieldsMapper implements IPagingFieldMapper<CashAccountTransactionOverviewFields> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountTransactionOverviewFieldsMapper.class);
	
	@Override
	public Expression<?> map(CashAccountTransactionOverviewFields pagingField) {
		switch (pagingField) {
			case CATEGORY_NAME:
				return QCategory.category.name;
			case VALUTA_DATE:
				return QCashAccountTransaction.cashAccountTransaction.valutaDate;
			case USAGE:
				return QCashAccountTransaction.cashAccountTransaction.usage;
			case VALUE:
				return QCashAccountTransaction.cashAccountTransaction.value;
			case DESCRIPTION:
				return QCashAccountTransaction.cashAccountTransaction.description;
			case REFERENCE_BANK:
				return QCashAccountTransaction.cashAccountTransaction.referenceBank;
			case REFERENCE_IBAN:
				return QCashAccountTransaction.cashAccountTransaction.referenceIban;
			case REFERENCE:
				return QCashAccountTransaction.cashAccountTransaction.reference;
			case CREDITOR_IDENTIFIER:
				return QCashAccountTransaction.cashAccountTransaction.creditorIdentifier;
			case MANDATE:
				return QCashAccountTransaction.cashAccountTransaction.mandate;
			case CREATION_DATE:
				return QCashAccountTransaction.cashAccountTransaction.creationDate;
			default:
				String message = String.format("Could not map enum value '%s' of type '%s' to expression.", pagingField, pagingField.getClass().getSimpleName());
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
