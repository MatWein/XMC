package io.github.matwein.xmc.be.repositories.ccf;

import io.github.matwein.xmc.be.entities.depot.CurrencyConversionFactor;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.ccf.CurrencyConversionFactorOverviewFields;
import io.github.matwein.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static io.github.matwein.xmc.be.common.QueryUtil.fromPage;
import static io.github.matwein.xmc.be.common.QueryUtil.toPageable;

public interface CurrencyConversionFactorRepository extends JpaRepository<CurrencyConversionFactor, Long> {
	default QueryResults<DtoCurrencyConversionFactor> loadOverview(PagingParams<CurrencyConversionFactorOverviewFields> pagingParams) {
		return fromPage(loadOverview$(
				toPageable(pagingParams, CurrencyConversionFactorOverviewFields.INPUT_DATE, Order.DESC),
				StringUtils.defaultString(pagingParams.getFilter())));
	}
	
	@Query("""
		select new io.github.matwein.xmc.common.stubs.ccf.DtoCurrencyConversionFactor(
			ccf.id, ccf.inputDate, ccf.currency, ccf.factorToEur
		)
		from CurrencyConversionFactor ccf
		where ccf.currency ilike '%' || :filter || '%'
	""")
	Page<DtoCurrencyConversionFactor> loadOverview$(Pageable pageable, @Param("filter") String filter);
}
