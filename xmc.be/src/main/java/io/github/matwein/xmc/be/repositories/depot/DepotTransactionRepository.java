package io.github.matwein.xmc.be.repositories.depot;

import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotTransaction;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransactionOverview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static io.github.matwein.xmc.be.common.QueryUtil.fromPage;
import static io.github.matwein.xmc.be.common.QueryUtil.toPageable;

public interface DepotTransactionRepository extends JpaRepository<DepotTransaction, Long> {
	default QueryResults<DtoDepotTransactionOverview> loadOverview(Depot depotEntity, PagingParams<DepotTransactionOverviewFields> pagingParams) {
		Pageable pageable = toPageable(pagingParams, DepotTransactionOverviewFields.VALUTA_DATE, Order.DESC);
		
		Sort extendedSort = pageable.getSort()
				.and(Sort.by("dt.creationDate").descending())
				.and(Sort.by("dt.id").descending());
		
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), extendedSort);
		
		return fromPage(loadOverview$(
				pageable,
				depotEntity.getId(),
				StringUtils.defaultString(pagingParams.getFilter())));
	}
	
	@Query("""
		select new io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransactionOverview(
			dt.id, dt.isin, dt.valutaDate, dt.amount, dt.course, dt.value, dt.description, dt.currency, dt.creationDate, s.wkn, s.name
		)
		from DepotTransaction dt
		left join Stock s on s.isin = dt.isin
		where (
			dt.isin ilike '%' || :filter || '%'
			or dt.description ilike '%' || :filter || '%'
			or s.wkn ilike '%' || :filter || '%'
			or s.name ilike '%' || :filter || '%'
		) and dt.deletionDate is null and dt.depot.id = :depotId
	""")
	Page<DtoDepotTransactionOverview> loadOverview$(
			Pageable pageable,
			@Param("depotId") Long depotId,
			@Param("filter") String filter);
}
