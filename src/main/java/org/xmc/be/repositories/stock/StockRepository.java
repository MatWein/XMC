package org.xmc.be.repositories.stock;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmc.be.common.QueryUtil;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.stocks.DtoStockOverview;
import org.xmc.common.stubs.stocks.StockOverviewFields;

import static org.xmc.be.entities.depot.QStock.stock;
import static org.xmc.be.entities.depot.QStockCategory.stockCategory;

@Repository
public class StockRepository {
	private final QueryUtil queryUtil;
	
	@Autowired
	public StockRepository(QueryUtil queryUtil) {
		this.queryUtil = queryUtil;
	}
	
	public QueryResults<DtoStockOverview> loadOverview(PagingParams<StockOverviewFields> pagingParams) {
		String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
		BooleanExpression predicate = stock.name.likeIgnoreCase(filter)
				.or(stock.isin.likeIgnoreCase(filter))
				.or(stock.wkn.likeIgnoreCase(filter))
				.or(stockCategory.name.likeIgnoreCase(filter));
		
		return queryUtil.createPagedQuery(pagingParams, StockOverviewFields.NAME, Order.ASC)
				.select(Projections.constructor(DtoStockOverview.class,
						stock.id, stock.isin, stock.name, stock.wkn, stockCategory.id,
						stockCategory.name, stock.creationDate))
				.from(stock)
				.leftJoin(stock.stockCategory(), stockCategory)
				.where(predicate)
				.fetchResults();
	}
}
