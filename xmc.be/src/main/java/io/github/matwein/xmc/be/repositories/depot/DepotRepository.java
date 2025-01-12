package io.github.matwein.xmc.be.repositories.depot;

import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.depot.DepotOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.DtoDepotOverview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static io.github.matwein.xmc.be.common.QueryUtil.fromPage;
import static io.github.matwein.xmc.be.common.QueryUtil.toPageable;

public interface DepotRepository extends JpaRepository<Depot, Long> {
	default QueryResults<DtoDepotOverview> loadOverview(PagingParams<DepotOverviewFields> pagingParams) {
		return fromPage(loadOverview$(toPageable(pagingParams, DepotOverviewFields.NAME, Order.ASC), StringUtils.defaultString(pagingParams.getFilter())));
	}
	
	@Query("""
		select new io.github.matwein.xmc.common.stubs.depot.DtoDepotOverview(
			d.id, d.number, d.name, d.color, d.creationDate, ld.saldo, ld.deliveryDate,
			b.id, b.name, b.bic, b.blz, l.rawData
		)
		from Depot d
		inner join d.bank b
		left join d.lastDelivery ld
		left join b.logo l
		where d.deletionDate is null and (
			d.name ilike '%' || :filter || '%'
			or d.number ilike '%' || :filter || '%'
			or b.bic ilike '%' || :filter || '%'
			or b.blz ilike '%' || :filter || '%'
			or b.name ilike '%' || :filter || '%'
		)
	""")
	Page<DtoDepotOverview> loadOverview$(Pageable pageable, @Param("filter") String filter);
}
