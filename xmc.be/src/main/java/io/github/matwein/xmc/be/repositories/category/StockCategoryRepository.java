package io.github.matwein.xmc.be.repositories.category;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Projections;
import io.github.matwein.xmc.be.common.QueryUtil;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.category.DtoStockCategoryOverview;
import io.github.matwein.xmc.common.stubs.category.StockCategoryOverviewFields;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static io.github.matwein.xmc.be.entities.depot.QStockCategory.stockCategory;

@Repository
public class StockCategoryRepository {
    private final QueryUtil queryUtil;
	
	@Autowired
    public StockCategoryRepository(QueryUtil queryUtil) {
        this.queryUtil = queryUtil;
	}

    public QueryResults<DtoStockCategoryOverview> loadOverview(PagingParams<StockCategoryOverviewFields> pagingParams) {
        String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";

        return queryUtil.createPagedQuery(pagingParams, StockCategoryOverviewFields.NAME, Order.ASC)
                .select(Projections.bean(DtoStockCategoryOverview.class,
		                stockCategory.id, stockCategory.name, stockCategory.creationDate))
                .from(stockCategory)
                .where(stockCategory.deletionDate.isNull())
                .where(stockCategory.name.likeIgnoreCase(filter))
                .fetchResults();
    }
}
