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
import org.xmc.common.stubs.category.DtoStockCategoryOverview;
import org.xmc.common.stubs.category.StockCategoryOverviewFields;

import static org.xmc.be.entities.depot.QStockCategory.stockCategory;

@Repository
public class StockCategoryRepository {
    private final QueryUtil queryUtil;
	
	@Autowired
    public StockCategoryRepository(QueryUtil queryUtil) {
        this.queryUtil = queryUtil;
	}

    public QueryResults<DtoStockCategoryOverview> loadOverview(PagingParams<StockCategoryOverviewFields> pagingParams) {
        String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
        BooleanExpression predicate = stockCategory.name.likeIgnoreCase(filter);

        return queryUtil.createPagedQuery(pagingParams, StockCategoryOverviewFields.NAME, Order.ASC)
                .select(Projections.constructor(DtoStockCategoryOverview.class,
		                stockCategory.id, stockCategory.name, stockCategory.creationDate))
                .from(stockCategory)
                .where(ExpressionUtils.allOf(predicate, stockCategory.deletionDate.isNull()))
                .fetchResults();
    }
}
