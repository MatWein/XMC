package io.github.matwein.xmc.be.common.pagination.mapper;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.be.entities.depot.QStockCategory;
import io.github.matwein.xmc.common.stubs.category.StockCategoryOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class StockCategoryOverviewFieldsMapper implements IPagingFieldMapper<StockCategoryOverviewFields> {
	@Override
	public Expression<?> map(StockCategoryOverviewFields pagingField) {
		return switch (pagingField) {
			case NAME -> QStockCategory.stockCategory.name;
			case CREATION_DATE -> QStockCategory.stockCategory.creationDate;
		};
	}
}
