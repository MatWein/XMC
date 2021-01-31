package org.xmc.be.repositories.depot;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmc.be.common.QueryUtil;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryOverview;

import java.util.List;
import java.util.Optional;

import static org.xmc.be.entities.depot.QDepotDelivery.depotDelivery;

@Repository
public class DepotDeliveryRepository {
	private final QueryUtil queryUtil;
	
	@Autowired
	public DepotDeliveryRepository(QueryUtil queryUtil) {
		this.queryUtil = queryUtil;
	}
	
	public QueryResults<DtoDepotDeliveryOverview> loadOverview(Depot depotEntity, PagingParams<DepotDeliveryOverviewFields> pagingParams) {
		Predicate predicate = depotDelivery.deletionDate.isNull()
				.and(depotDelivery.depot().eq(depotEntity));
		
		return queryUtil.createPagedQuery(pagingParams, DepotDeliveryOverviewFields.DELIVERY_DATE, Order.DESC)
				.select(Projections.constructor(DtoDepotDeliveryOverview.class,
						depotDelivery.id, depotDelivery.deliveryDate, depotDelivery.saldo,
						depotDelivery.creationDate))
				.from(depotDelivery)
				.where(predicate)
				.fetchResults();
	}
	
	public Optional<DepotDelivery> loadMostRecentDeliveryOfDepot(Depot depotEntity) {
		List<DepotDelivery> results = queryUtil.createQuery()
				.select(depotDelivery)
				.from(depotDelivery)
				.where(depotDelivery.depot().eq(depotEntity))
				.where(depotDelivery.deletionDate.isNull())
				.orderBy(new OrderSpecifier<>(Order.DESC, depotDelivery.deliveryDate))
				.limit(1)
				.fetch();
		
		if (results.size() == 1) {
			return Optional.of(results.get(0));
		} else {
			return Optional.empty();
		}
	}
}
