package io.github.matwein.xmc.be.common.pagination.mapper;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.be.entities.cashaccount.QCategory;
import io.github.matwein.xmc.common.stubs.category.CategoryOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class CategoryOverviewFieldsMapper implements IPagingFieldMapper<CategoryOverviewFields> {
	@Override
	public Expression<?> map(CategoryOverviewFields pagingField) {
		return switch (pagingField) {
			case NAME -> QCategory.category.name;
			case CREATION_DATE -> QCategory.category.creationDate;
		};
	}
}
