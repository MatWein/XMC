package io.github.matwein.xmc.be.common.pagination;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.entities.depot.QStock;
import io.github.matwein.xmc.be.entities.depot.QStockCategory;
import io.github.matwein.xmc.common.stubs.stocks.StockOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StockOverviewFieldsMapper implements IPagingFieldMapper<StockOverviewFields> {
	private static final Logger LOGGER = LoggerFactory.getLogger(StockOverviewFieldsMapper.class);
	
	@Override
	public Expression<?> map(StockOverviewFields pagingField) {
		switch (pagingField) {
			case ISIN:
				return QStock.stock.isin;
			case WKN:
				return QStock.stock.wkn;
			case NAME:
				return QStock.stock.name;
			case STOCK_CATEGORY:
				return QStockCategory.stockCategory.name;
			case CREATION_DATE:
				return QStock.stock.creationDate;
			default:
				String message = String.format("Could not map enum value '%s' of type '%s' to expression.", pagingField, pagingField.getClass().getSimpleName());
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
