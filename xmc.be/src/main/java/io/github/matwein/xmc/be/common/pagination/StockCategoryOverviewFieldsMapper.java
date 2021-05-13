package io.github.matwein.xmc.be.common.pagination;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.entities.depot.QStockCategory;
import io.github.matwein.xmc.common.stubs.category.StockCategoryOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StockCategoryOverviewFieldsMapper implements IPagingFieldMapper<StockCategoryOverviewFields> {
	private static final Logger LOGGER = LoggerFactory.getLogger(StockCategoryOverviewFieldsMapper.class);
	
	@Override
	public Expression<?> map(StockCategoryOverviewFields pagingField) {
		switch (pagingField) {
			case NAME:
				return QStockCategory.stockCategory.name;
			case CREATION_DATE:
				return QStockCategory.stockCategory.creationDate;
			default:
				String message = String.format("Could not map enum value '%s' of type '%s' to expression.", pagingField, pagingField.getClass().getSimpleName());
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
