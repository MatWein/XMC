package io.github.matwein.xmc.be.common.pagination.mapper;

import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.common.stubs.category.CategoryOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class CategoryOverviewFieldsMapper implements IPagingFieldMapper<CategoryOverviewFields> {
	@Override
	public String map(CategoryOverviewFields pagingField) {
		return switch (pagingField) {
			case NAME -> "c.name";
			case CREATION_DATE -> "c.creationDate";
		};
	}
}
