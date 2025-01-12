package io.github.matwein.xmc.be.common.pagination.mapper;

import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class DepotItemOverviewFieldsMapper implements IPagingFieldMapper<DepotItemOverviewFields> {
	@Override
	public String map(DepotItemOverviewFields pagingField) {
		return switch (pagingField) {
			case ISIN -> "di.isin";
			case WKN -> "s.wkn";
			case NAME -> "s.name";
			case AMOUNT -> "di.amount";
			case COURSE -> "di.course";
			case VALUE -> "di.value";
			case CURRENCY -> "di.currency";
			case CREATION_DATE -> "di.creationDate";
		};
	}
}
