package io.github.matwein.xmc.be.repositories.depot;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import io.github.matwein.xmc.be.common.QueryUtil;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemOverview;

import static io.github.matwein.xmc.be.entities.depot.QDepotItem.depotItem;
import static io.github.matwein.xmc.be.entities.depot.QStock.stock;

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
						depotItem.value, depotItem.currency, depotItem.creationDate,
						stock.wkn, stock.name))
				.from(depotItem)
				.leftJoin(stock).on(stock.isin.eq(depotItem.isin))
				.where(predicate)
				.fetchResults();
	}
	
	private Predicate calculatePredicate(DepotDelivery depotDeliveryEntity, PagingParams<DepotItemOverviewFields> pagingParams) {
		String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
		
		Predicate predicate = depotItem.isin.likeIgnoreCase(filter)
				.or(stock.wkn.likeIgnoreCase(filter))
				.or(stock.name.likeIgnoreCase(filter));
		
		return ExpressionUtils.allOf(predicate,
				depotItem.deletionDate.isNull(),
				depotItem.delivery().eq(depotDeliveryEntity));
	}
}
