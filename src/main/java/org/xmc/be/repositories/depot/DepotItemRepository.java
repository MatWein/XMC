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
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.depot.deliveries.DepotItemOverviewFields;
import org.xmc.common.stubs.depot.deliveries.DtoDepotItemOverview;

import static org.xmc.be.entities.depot.QDepotItem.depotItem;

@Repository
public class DepotItemRepository {
	private final QueryUtil queryUtil;
	
	@Autowired
	public DepotItemRepository(QueryUtil queryUtil) {
		this.queryUtil = queryUtil;
	}
	
	public QueryResults<DtoDepotItemOverview> loadOverview(DepotDelivery depotDeliveryEntity, PagingParams<DepotItemOverviewFields> pagingParams) {
		Predicate predicate = calculatePredicate(depotDeliveryEntity, pagingParams);
		
		return queryUtil.createPagedQuery(pagingParams, DepotItemOverviewFields.ISIN, Order.ASC)
				.select(Projections.constructor(DtoDepotItemOverview.class,
						depotItem.id, depotItem.isin, depotItem.amount, depotItem.course,
						depotItem.value, depotItem.currency, depotItem.creationDate))
				.from(depotItem)
				.where(predicate)
				.fetchResults();
	}
	
	private Predicate calculatePredicate(DepotDelivery depotDeliveryEntity, PagingParams<DepotItemOverviewFields> pagingParams) {
		String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
		
		return ExpressionUtils.allOf(
				depotItem.isin.likeIgnoreCase(filter),
				depotItem.deletionDate.isNull(),
				depotItem.delivery().eq(depotDeliveryEntity));
	}
}
