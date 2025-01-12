package io.github.matwein.xmc.be.common.pagination.mapper;

import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.common.stubs.category.StockCategoryOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class StockCategoryOverviewFieldsMapper implements IPagingFieldMapper<StockCategoryOverviewFields> {
	@Override
	public String map(StockCategoryOverviewFields pagingField) {
		return switch (pagingField) {
			case NAME -> "sc.name";
			case CREATION_DATE -> "sc.creationDate";
		};
	}
}
