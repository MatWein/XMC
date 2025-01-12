package io.github.matwein.xmc.be.repositories.depot;

import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.be.entities.depot.DepotItem;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemOverview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static io.github.matwein.xmc.be.common.QueryUtil.fromPage;
import static io.github.matwein.xmc.be.common.QueryUtil.toPageable;

public interface DepotItemRepository extends JpaRepository<DepotItem, Long> {
	default QueryResults<DtoDepotItemOverview> loadOverview(DepotDelivery depotDeliveryEntity, PagingParams<DepotItemOverviewFields> pagingParams) {
		return fromPage(loadOverview$(
				toPageable(pagingParams, DepotItemOverviewFields.ISIN, Order.ASC),
				StringUtils.defaultString(pagingParams.getFilter()),
				depotDeliveryEntity.getId()));
	}
	
	@Query("""
		select new io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemOverview(
			di.id, di.isin, di.amount, di.course, di.value, di.currency, di.creationDate, s.wkn, s.name
		)
		from DepotItem di
		left join Stock s on s.isin = di.isin
		where di.deletionDate is null and di.delivery.id = :deliveryId and (
			di.isin ilike '%' || :filter || '%'
			or s.wkn ilike '%' || :filter || '%'
			or s.name ilike '%' || :filter || '%'
		)
	""")
	Page<DtoDepotItemOverview> loadOverview$(
			Pageable pageable,
			@Param("filter") String filter,
			@Param("deliveryId") Long deliveryId);
}
