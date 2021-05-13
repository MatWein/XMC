package io.github.matwein.xmc.be.common.pagination;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.entities.depot.QDepotTransaction;
import io.github.matwein.xmc.be.entities.depot.QStock;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DepotTransactionOverviewFieldsMapper implements IPagingFieldMapper<DepotTransactionOverviewFields> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotTransactionOverviewFieldsMapper.class);
	
	@Override
	public Expression<?> map(DepotTransactionOverviewFields pagingField) {
		switch (pagingField) {
			case VALUTA_DATE:
				return QDepotTransaction.depotTransaction.valutaDate;
			case ISIN:
				return QDepotTransaction.depotTransaction.isin;
			case WKN:
				return QStock.stock.wkn;
			case NAME:
				return QStock.stock.name;
			case AMOUNT:
				return QDepotTransaction.depotTransaction.amount;
			case COURSE:
				return QDepotTransaction.depotTransaction.course;
			case VALUE:
				return QDepotTransaction.depotTransaction.value;
			case DESCRIPTION:
				return QDepotTransaction.depotTransaction.description;
			case CURRENCY:
				return QDepotTransaction.depotTransaction.currency;
			case CREATION_DATE:
				return QDepotTransaction.depotTransaction.creationDate;
			default:
				String message = String.format("Could not map enum value '%s' of type '%s' to expression.", pagingField, pagingField.getClass().getSimpleName());
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
