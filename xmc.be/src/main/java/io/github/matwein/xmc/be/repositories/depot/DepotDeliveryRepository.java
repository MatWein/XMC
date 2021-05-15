package io.github.matwein.xmc.be.repositories.depot;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import io.github.matwein.xmc.be.common.QueryUtil;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.common.stubs.Money;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryOverview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.github.matwein.xmc.be.entities.depot.QDepotDelivery.depotDelivery;

@Repository
public class DepotDeliveryRepository {
	private final QueryUtil queryUtil;
	
	@Autowired
	public DepotDeliveryRepository(QueryUtil queryUtil) {
		this.queryUtil = queryUtil;
	}
	
	public QueryResults<DtoDepotDeliveryOverview> loadOverview(Depot depotEntity, PagingParams<DepotDeliveryOverviewFields> pagingParams) {
		return queryUtil.createPagedQuery(pagingParams, DepotDeliveryOverviewFields.DELIVERY_DATE, Order.DESC)
				.select(Projections.bean(DtoDepotDeliveryOverview.class,
						depotDelivery.id, depotDelivery.deliveryDate,
						Projections.bean(Money.class, depotDelivery.saldo.as("value")).as("saldo"),
						depotDelivery.creationDate))
				.from(depotDelivery)
				.where(depotDelivery.deletionDate.isNull())
				.where(depotDelivery.depot().eq(depotEntity))
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
