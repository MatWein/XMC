package io.github.matwein.xmc.be.repositories.stock;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.github.matwein.xmc.be.common.QueryUtil;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.category.DtoStockCategory;
import io.github.matwein.xmc.common.stubs.stocks.DtoMinimalStock;
import io.github.matwein.xmc.common.stubs.stocks.DtoStockOverview;
import io.github.matwein.xmc.common.stubs.stocks.StockOverviewFields;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.OrderSpecifier.NullHandling.NullsLast;
import static io.github.matwein.xmc.be.entities.depot.QStock.stock;
import static io.github.matwein.xmc.be.entities.depot.QStockCategory.stockCategory;

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
				.select(Projections.bean(DtoStockOverview.class,
						stock.id, stock.isin, stock.name, stock.wkn, stock.creationDate,
						ExpressionUtils.as(Projections.bean(DtoStockCategory.class, stockCategory.id, stockCategory.name).skipNulls(), "stockCategory")))
				.from(stock)
				.leftJoin(stock.stockCategory(), stockCategory)
				.where(predicate)
				.fetchResults();
	}
	
	public List<DtoMinimalStock> loadAllStocks(String searchValue, int limit) {
		String filter = "%" + StringUtils.defaultString(searchValue) + "%";
		BooleanExpression predicate = stock.name.likeIgnoreCase(filter)
				.or(stock.isin.likeIgnoreCase(filter))
				.or(stock.wkn.likeIgnoreCase(filter));
		
		return queryUtil.createQuery()
				.select(Projections.bean(DtoMinimalStock.class, stock.isin, stock.wkn, stock.name))
				.from(stock)
				.where(predicate)
				.orderBy(new OrderSpecifier(Order.ASC, stock.isin, NullsLast))
				.limit(limit)
				.fetch();
	}
}
