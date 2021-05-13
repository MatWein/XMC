package io.github.matwein.xmc.be.common.pagination;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.entities.cashaccount.QCategory;
import io.github.matwein.xmc.common.stubs.category.CategoryOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CategoryOverviewFieldsMapper implements IPagingFieldMapper<CategoryOverviewFields> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryOverviewFieldsMapper.class);
	
	@Override
	public Expression<?> map(CategoryOverviewFields pagingField) {
		switch (pagingField) {
			case NAME:
				return QCategory.category.name;
			case CREATION_DATE:
				return QCategory.category.creationDate;
			default:
				String message = String.format("Could not map enum value '%s' of type '%s' to expression.", pagingField, pagingField.getClass().getSimpleName());
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
