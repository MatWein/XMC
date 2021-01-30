package org.xmc.be.repositories.depot;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmc.be.common.QueryUtil;
import org.xmc.be.entities.depot.Depot;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransactionOverview;

import static org.xmc.be.entities.depot.QDepot.depot;
import static org.xmc.be.entities.depot.QDepotTransaction.depotTransaction;

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
						depotTransaction.creationDate))
				.from(depotTransaction)
				.innerJoin(depotTransaction.depot(), depot)
				.where(predicate)
				.fetchResults();
	}
	
	private Predicate calculatePredicate(Depot depot, PagingParams<DepotTransactionOverviewFields> pagingParams) {
		String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
		
		Predicate predicate = depotTransaction.isin.likeIgnoreCase(filter)
				.or(depotTransaction.description.likeIgnoreCase(filter));
		
		predicate = ExpressionUtils.allOf(predicate,
				depotTransaction.deletionDate.isNull(),
				depotTransaction.depot().eq(depot));
		
		return predicate;
	}
}
