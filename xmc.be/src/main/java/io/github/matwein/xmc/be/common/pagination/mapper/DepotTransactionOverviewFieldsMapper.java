package io.github.matwein.xmc.be.common.pagination.mapper;

import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class DepotTransactionOverviewFieldsMapper implements IPagingFieldMapper<DepotTransactionOverviewFields> {
	@Override
	public String map(DepotTransactionOverviewFields pagingField) {
		return switch (pagingField) {
			case VALUTA_DATE -> "dt.valutaDate";
			case ISIN -> "dt.isin";
			case WKN -> "s.wkn";
			case NAME -> "s.name";
			case AMOUNT -> "dt.amount";
			case COURSE -> "dt.course";
			case VALUE -> "dt.value";
			case DESCRIPTION -> "dt.description";
			case CURRENCY -> "dt.currency";
			case CREATION_DATE -> "dt.creationDate";
		};
	}
}
