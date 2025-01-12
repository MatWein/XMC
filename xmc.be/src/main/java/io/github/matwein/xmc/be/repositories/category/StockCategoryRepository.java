package io.github.matwein.xmc.be.repositories.category;

import io.github.matwein.xmc.be.entities.depot.StockCategory;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.category.DtoStockCategoryOverview;
import io.github.matwein.xmc.common.stubs.category.StockCategoryOverviewFields;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static io.github.matwein.xmc.be.common.QueryUtil.fromPage;
import static io.github.matwein.xmc.be.common.QueryUtil.toPageable;

public interface StockCategoryRepository extends JpaRepository<StockCategory, Long> {
    default QueryResults<DtoStockCategoryOverview> loadOverview(PagingParams<StockCategoryOverviewFields> pagingParams) {
		return fromPage(loadOverview$(toPageable(pagingParams, StockCategoryOverviewFields.NAME, Order.ASC), StringUtils.defaultString(pagingParams.getFilter())));
    }
	
	@Query("""
		select new io.github.matwein.xmc.common.stubs.category.DtoStockCategoryOverview(sc.id, sc.name, sc.creationDate)
		from StockCategory sc
		where sc.deletionDate is null and sc.name ilike '%' || :filter || '%'
	""")
	Page<DtoStockCategoryOverview> loadOverview$(Pageable pageable, @Param("filter") String filter);
}
