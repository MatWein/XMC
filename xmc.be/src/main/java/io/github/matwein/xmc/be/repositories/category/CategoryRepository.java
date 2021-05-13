package io.github.matwein.xmc.be.repositories.category;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.github.matwein.xmc.be.common.QueryUtil;
import io.github.matwein.xmc.be.entities.cashaccount.Category;
import io.github.matwein.xmc.be.services.category.mapper.CategoryToDtoCategoryMapper;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.category.CategoryOverviewFields;
import io.github.matwein.xmc.common.stubs.category.DtoCategory;
import io.github.matwein.xmc.common.stubs.category.DtoCategoryOverview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.matwein.xmc.be.entities.QBinaryData.binaryData;
import static io.github.matwein.xmc.be.entities.cashaccount.QCategory.category;

@Repository
public class CategoryRepository {
    private final QueryUtil queryUtil;
	private final CategoryToDtoCategoryMapper categoryToDtoCategoryMapper;
	
	@Autowired
    public CategoryRepository(QueryUtil queryUtil, CategoryToDtoCategoryMapper categoryToDtoCategoryMapper) {
        this.queryUtil = queryUtil;
		this.categoryToDtoCategoryMapper = categoryToDtoCategoryMapper;
	}

    public QueryResults<DtoCategoryOverview> loadOverview(PagingParams<CategoryOverviewFields> pagingParams) {
        String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
        BooleanExpression predicate = category.name.likeIgnoreCase(filter);

        return queryUtil.createPagedQuery(pagingParams, CategoryOverviewFields.NAME, Order.ASC)
                .select(Projections.bean(DtoCategoryOverview.class,
                        category.id, category.name,
                        binaryData.rawData.as("icon"), category.creationDate))
                .from(category)
                .leftJoin(category.icon(), binaryData)
                .where(ExpressionUtils.allOf(predicate, category.deletionDate.isNull()))
                .fetchResults();
    }
    
    public Map<String, DtoCategory> loadAllCategories() {
	    List<Category> categories = queryUtil.createQuery()
			    .select(category)
			    .from(category)
			    .fetch();
	
	    return categories.stream()
			    .map(categoryToDtoCategoryMapper)
			    .collect(Collectors.toMap(DtoCategory::getName, Function.identity()));
    }
}
