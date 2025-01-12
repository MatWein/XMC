package io.github.matwein.xmc.be.repositories.category;

import io.github.matwein.xmc.be.entities.cashaccount.Category;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.category.CategoryOverviewFields;
import io.github.matwein.xmc.common.stubs.category.DtoCategoryOverview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static io.github.matwein.xmc.be.common.QueryUtil.fromPage;
import static io.github.matwein.xmc.be.common.QueryUtil.toPageable;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    default QueryResults<DtoCategoryOverview> loadOverview(PagingParams<CategoryOverviewFields> pagingParams) {
	    return fromPage(loadOverview$(toPageable(pagingParams, CategoryOverviewFields.NAME, Order.ASC), StringUtils.defaultString(pagingParams.getFilter())));
    }
	
	@Query("""
		select new io.github.matwein.xmc.common.stubs.category.DtoCategoryOverview(c.id, c.name, i.rawData, c.creationDate)
		from Category c
		left join c.icon i
		where c.deletionDate is null and c.name ilike '%' || :filter || '%'
	""")
	Page<DtoCategoryOverview> loadOverview$(Pageable pageable, @Param("filter") String filter);
}
