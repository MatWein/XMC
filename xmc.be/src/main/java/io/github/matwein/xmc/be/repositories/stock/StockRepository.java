package io.github.matwein.xmc.be.repositories.stock;

import io.github.matwein.xmc.be.entities.depot.Stock;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.stocks.DtoMinimalStock;
import io.github.matwein.xmc.common.stubs.stocks.DtoStockOverview;
import io.github.matwein.xmc.common.stubs.stocks.StockOverviewFields;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static io.github.matwein.xmc.be.common.QueryUtil.fromPage;
import static io.github.matwein.xmc.be.common.QueryUtil.toPageable;

public interface StockRepository extends JpaRepository<Stock, Long> {
	default QueryResults<DtoStockOverview> loadOverview(PagingParams<StockOverviewFields> pagingParams) {
		return fromPage(loadOverview$(
				toPageable(pagingParams, StockOverviewFields.NAME, Order.ASC),
				StringUtils.defaultString(pagingParams.getFilter())));
	}
	
	@Query("""
		select new io.github.matwein.xmc.common.stubs.stocks.DtoStockOverview(
			s.id, s.isin, s.name, s.wkn, s.creationDate, sc.id, sc.name
		)
		from Stock s
		left join s.stockCategory sc
		where (
			s.name ilike '%' || :filter || '%'
			or s.isin ilike '%' || :filter || '%'
			or s.wkn ilike '%' || :filter || '%'
			or sc.name ilike '%' || :filter || '%'
		)
	""")
	Page<DtoStockOverview> loadOverview$(Pageable pageable, @Param("filter") String filter);
	
	@Query("""
		select new io.github.matwein.xmc.common.stubs.stocks.DtoMinimalStock(s.isin, s.wkn, s.name)
		from Stock s
		where (
			s.name ilike '%' || :searchValue || '%'
			or s.isin ilike '%' || :searchValue || '%'
			or s.wkn ilike '%' || :searchValue || '%'
		)
		order by s.isin nulls last limit :limit
	""")
	List<DtoMinimalStock> loadAllStocks(@Param("searchValue") String searchValue, @Param("limit") int limit);
}
