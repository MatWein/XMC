package org.xmc.be.repositories.depot;

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
import org.xmc.common.stubs.depot.DepotOverviewFields;
import org.xmc.common.stubs.depot.DtoDepotOverview;

import static org.xmc.be.entities.QBank.bank;
import static org.xmc.be.entities.QBinaryData.binaryData;
import static org.xmc.be.entities.depot.QDepot.depot;
import static org.xmc.be.entities.depot.QDepotDelivery.depotDelivery;

@Repository
public class DepotRepository {
	private final QueryUtil queryUtil;
	
	@Autowired
	public DepotRepository(QueryUtil queryUtil) {
		this.queryUtil = queryUtil;
	}
	
	public QueryResults<DtoDepotOverview> loadOverview(PagingParams<DepotOverviewFields> pagingParams) {
		String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
		BooleanExpression predicate = depot.name.likeIgnoreCase(filter)
				.or(depot.number.likeIgnoreCase(filter))
				.or(bank.bic.likeIgnoreCase(filter))
				.or(bank.blz.likeIgnoreCase(filter))
				.or(bank.name.likeIgnoreCase(filter));
		
		return queryUtil.createPagedQuery(pagingParams, DepotOverviewFields.NAME, Order.ASC)
				.select(Projections.constructor(DtoDepotOverview.class,
						depot.id, depot.number, depot.name,
						depot.creationDate, depotDelivery.saldo, depotDelivery.deliveryDate,
						bank.id, bank.name, bank.bic, bank.blz,
						binaryData.rawData))
				.from(depot)
				.innerJoin(depot.bank(), bank)
				.leftJoin(depot.lastDelivery(), depotDelivery)
				.leftJoin(bank.logo(), binaryData)
				.where(ExpressionUtils.allOf(predicate, depot.deletionDate.isNull()))
				.fetchResults();
	}
}
