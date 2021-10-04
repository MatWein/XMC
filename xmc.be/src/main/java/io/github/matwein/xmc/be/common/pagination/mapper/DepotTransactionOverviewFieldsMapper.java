package io.github.matwein.xmc.be.common.pagination.mapper;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.be.entities.depot.QDepotTransaction;
import io.github.matwein.xmc.be.entities.depot.QStock;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class DepotTransactionOverviewFieldsMapper implements IPagingFieldMapper<DepotTransactionOverviewFields> {
	@Override
	public Expression<?> map(DepotTransactionOverviewFields pagingField) {
		return switch (pagingField) {
			case VALUTA_DATE -> QDepotTransaction.depotTransaction.valutaDate;
			case ISIN -> QDepotTransaction.depotTransaction.isin;
			case WKN -> QStock.stock.wkn;
			case NAME -> QStock.stock.name;
			case AMOUNT -> QDepotTransaction.depotTransaction.amount;
			case COURSE -> QDepotTransaction.depotTransaction.course;
			case VALUE -> QDepotTransaction.depotTransaction.value;
			case DESCRIPTION -> QDepotTransaction.depotTransaction.description;
			case CURRENCY -> QDepotTransaction.depotTransaction.currency;
			case CREATION_DATE -> QDepotTransaction.depotTransaction.creationDate;
		};
	}
}
