package org.xmc.be.repositories.category;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmc.be.common.QueryUtil;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.category.CategoryOverviewFields;
import org.xmc.common.stubs.category.DtoCategoryOverview;

import static org.xmc.be.entities.QBinaryData.binaryData;
import static org.xmc.be.entities.QCategory.category;

@Repository
public class CategoryRepository {
    private final QueryUtil queryUtil;

    @Autowired
    public CategoryRepository(QueryUtil queryUtil) {
        this.queryUtil = queryUtil;
    }

    public QueryResults<DtoCategoryOverview> loadOverview(PagingParams<CategoryOverviewFields> pagingParams) {
        String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
        BooleanExpression predicate = category.name.likeIgnoreCase(filter);

        return queryUtil.createPagedQuery(pagingParams, CategoryOverviewFields.NAME, Order.ASC)
                .select(Projections.constructor(DtoCategoryOverview.class,
                        category.id, category.name,
                        binaryData.rawData, category.creationDate))
                .from(category)
                .leftJoin(category.icon(), binaryData)
                .where(ExpressionUtils.allOf(predicate, category.deletionDate.isNull()))
                .fetchResults();
    }
}
