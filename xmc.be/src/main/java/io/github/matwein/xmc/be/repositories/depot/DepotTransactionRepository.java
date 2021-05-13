package io.github.matwein.xmc.be.repositories.depot;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import io.github.matwein.xmc.be.common.QueryUtil;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransactionOverview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static io.github.matwein.xmc.be.entities.depot.QDepotTransaction.depotTransaction;
import static io.github.matwein.xmc.be.entities.depot.QStock.stock;

@Repository
public class DepotTransactionRepository {
	private final QueryUtil queryUtil;
	
	@Autowired
	public DepotTransactionRepository(QueryUtil queryUtil) {
		this.queryUtil = queryUtil;
	}
	
	public QueryResults<DtoDepotTransactionOverview> loadOverview(Depot depotEntity, PagingParams<DepotTransactionOverviewFields> pagingParams) {
		Predicate predicate = calculatePredicate(depotEntity, pagingParams);
		
		return queryUtil.createPagedQuery(pagingParams, DepotTransactionOverviewFields.VALUTA_DATE, Order.DESC)
				.select(Projections.constructor(DtoDepotTransactionOverview.class,
						depotTransaction.id, depotTransaction.isin, depotTransaction.valutaDate,
						depotTransaction.amount, depotTransaction.course, depotTransaction.value,
						depotTransaction.description, depotTransaction.currency,
						depotTransaction.creationDate, stock.wkn, stock.name))
				.from(depotTransaction)
				.leftJoin(stock).on(stock.isin.eq(depotTransaction.isin))
				.where(predicate)
				.fetchResults();
	}
	
	private Predicate calculatePredicate(Depot depot, PagingParams<DepotTransactionOverviewFields> pagingParams) {
		String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
		
		Predicate predicate = depotTransaction.isin.likeIgnoreCase(filter)
				.or(depotTransaction.description.likeIgnoreCase(filter))
				.or(stock.wkn.likeIgnoreCase(filter))
				.or(stock.name.likeIgnoreCase(filter));
		
		return ExpressionUtils.allOf(predicate,
				depotTransaction.deletionDate.isNull(),
				depotTransaction.depot().eq(depot));
	}
}
