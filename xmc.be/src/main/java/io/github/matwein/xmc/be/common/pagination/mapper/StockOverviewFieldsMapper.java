package io.github.matwein.xmc.be.common.pagination.mapper;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.be.entities.depot.QStock;
import io.github.matwein.xmc.be.entities.depot.QStockCategory;
import io.github.matwein.xmc.common.stubs.stocks.StockOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class StockOverviewFieldsMapper implements IPagingFieldMapper<StockOverviewFields> {
	@Override
	public Expression<?> map(StockOverviewFields pagingField) {
		return switch (pagingField) {
			case ISIN -> QStock.stock.isin;
			case WKN -> QStock.stock.wkn;
			case NAME -> QStock.stock.name;
			case STOCK_CATEGORY -> QStockCategory.stockCategory.name;
			case CREATION_DATE -> QStock.stock.creationDate;
		};
	}
}
