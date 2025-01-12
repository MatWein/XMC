package io.github.matwein.xmc.be.repositories.depot;

import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryOverview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static io.github.matwein.xmc.be.common.QueryUtil.fromPage;
import static io.github.matwein.xmc.be.common.QueryUtil.toPageable;

public interface DepotDeliveryRepository extends JpaRepository<DepotDelivery, Long> {
	default QueryResults<DtoDepotDeliveryOverview> loadOverview(Depot depotEntity, PagingParams<DepotDeliveryOverviewFields> pagingParams) {
		return fromPage(loadOverview$(
				toPageable(pagingParams, DepotDeliveryOverviewFields.DELIVERY_DATE, Order.DESC),
				StringUtils.defaultString(pagingParams.getFilter()),
				depotEntity.getId()));
	}
	
	@Query("""
		select new io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryOverview(
			dd.id, dd.deliveryDate, dd.saldo, dd.creationDate
		)
		from DepotDelivery dd
		where dd.deletionDate is null and dd.depot.id = :depotId
	""")
	Page<DtoDepotDeliveryOverview> loadOverview$(
			Pageable pageable,
			@Param("filter") String filter,
			@Param("depotId") Long depotId);
	
	@Query("""
		select dd from DepotDelivery dd
		where dd.depot = :depot and dd.deletionDate is null
		order by dd.deliveryDate desc limit 1
	""")
	Optional<DepotDelivery> loadMostRecentDeliveryOfDepot(@Param("depot") Depot depotEntity);
}
