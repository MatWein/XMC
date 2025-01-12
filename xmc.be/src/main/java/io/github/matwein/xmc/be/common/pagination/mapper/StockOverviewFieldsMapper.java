package io.github.matwein.xmc.be.common.pagination.mapper;

import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.common.stubs.stocks.StockOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class StockOverviewFieldsMapper implements IPagingFieldMapper<StockOverviewFields> {
	@Override
	public String map(StockOverviewFields pagingField) {
		return switch (pagingField) {
			case ISIN -> "s.isin";
			case WKN -> "s.wkn";
			case NAME -> "s.name";
			case STOCK_CATEGORY -> "sc.name";
			case CREATION_DATE -> "s.creationDate";
		};
	}
}
