package io.github.matwein.xmc.be.repositories.ccf;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.CurrencyConversionFactor;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.ccf.CurrencyConversionFactorOverviewFields;
import io.github.matwein.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

class CurrencyConversionFactorRepositoryTest extends IntegrationTest {
	@Autowired
	private CurrencyConversionFactorRepository repository;
	
	@Test
	void testLoadOverview() {
		PagingParams<CurrencyConversionFactorOverviewFields> pagingParams = new PagingParams<>();
		pagingParams.setOffset(0);
		pagingParams.setLimit(3);
		pagingParams.setSortBy(CurrencyConversionFactorOverviewFields.INPUT_DATE);
		pagingParams.setOrder(Order.DESC);
		
		graphGenerator.createCurrencyConversionFactor(
				"USD",
				LocalDateTime.of(2021, Month.JULY, 9, 0, 0, 0),
				BigDecimal.valueOf(0.83));
		
		CurrencyConversionFactor ccf2 = graphGenerator.createCurrencyConversionFactor(
				"AUD",
				LocalDateTime.of(2021, Month.JULY, 10, 0, 0, 0),
				BigDecimal.valueOf(0.11));
		
		CurrencyConversionFactor ccf3 = graphGenerator.createCurrencyConversionFactor(
				"JPY",
				LocalDateTime.of(2021, Month.JULY, 11, 0, 0, 0),
				BigDecimal.valueOf(0.22));
		
		CurrencyConversionFactor ccf4 = graphGenerator.createCurrencyConversionFactor(
				"RUB",
				LocalDateTime.of(2021, Month.JULY, 12, 0, 0, 0),
				BigDecimal.valueOf(0.33));
		
		flushAndClear();
		
		QueryResults<DtoCurrencyConversionFactor> result = repository.loadOverview(pagingParams);
		
		Assertions.assertEquals(4, result.getTotal());
		Assertions.assertEquals(3, result.getResults().size());
		Assertions.assertEquals(ccf4.getId(), result.getResults().get(0).getId());
		Assertions.assertEquals(ccf3.getId(), result.getResults().get(1).getId());
		Assertions.assertEquals(ccf2.getId(), result.getResults().get(2).getId());
	}
	
	@Test
	void testLoadOverview_CheckAllSortFields() {
		graphGenerator.createCurrencyConversionFactor(
				"USD",
				LocalDateTime.of(2021, Month.JULY, 9, 0, 0, 0),
				BigDecimal.valueOf(0.83));
		
		graphGenerator.createCurrencyConversionFactor(
				"AUD",
				LocalDateTime.of(2021, Month.JULY, 10, 0, 0, 0),
				BigDecimal.valueOf(0.11));
		
		graphGenerator.createCurrencyConversionFactor(
				"JPY",
				LocalDateTime.of(2021, Month.JULY, 11, 0, 0, 0),
				BigDecimal.valueOf(0.22));
		
		graphGenerator.createCurrencyConversionFactor(
				"RUB",
				LocalDateTime.of(2021, Month.JULY, 12, 0, 0, 0),
				BigDecimal.valueOf(0.33));
		
		flushAndClear();
		
		for (CurrencyConversionFactorOverviewFields field : CurrencyConversionFactorOverviewFields.values()) {
			QueryResults<DtoCurrencyConversionFactor> result = repository.loadOverview(new PagingParams<>(0, 3, field, Order.ASC, null));
			
			Assertions.assertEquals(4, result.getTotal());
			Assertions.assertEquals(3, result.getResults().size());
		}
	}
	
	@Test
	void testLoadOverview_CheckFields() {
		PagingParams<CurrencyConversionFactorOverviewFields> pagingParams = new PagingParams<>();
		pagingParams.setFilter("RUB");
		
		graphGenerator.createCurrencyConversionFactor(
				"USD",
				LocalDateTime.of(2021, Month.JULY, 9, 0, 0, 0),
				BigDecimal.valueOf(0.83));
		
		graphGenerator.createCurrencyConversionFactor(
				"AUD",
				LocalDateTime.of(2021, Month.JULY, 10, 0, 0, 0),
				BigDecimal.valueOf(0.11));
		
		graphGenerator.createCurrencyConversionFactor(
				"JPY",
				LocalDateTime.of(2021, Month.JULY, 11, 0, 0, 0),
				BigDecimal.valueOf(0.22));
		
		CurrencyConversionFactor ccf4 = graphGenerator.createCurrencyConversionFactor(
				"RUB",
				LocalDateTime.of(2021, Month.JULY, 12, 0, 0, 0),
				BigDecimal.valueOf(0.33));
		
		flushAndClear();
		
		QueryResults<DtoCurrencyConversionFactor> result = repository.loadOverview(pagingParams);
		
		Assertions.assertEquals(1, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		
		var dtoCurrencyConversionFactor = result.getResults().get(0);
		Assertions.assertEquals(ccf4.getId(), dtoCurrencyConversionFactor.getId());
		Assertions.assertEquals(ccf4.getCurrency(), dtoCurrencyConversionFactor.getCurrency());
		Assertions.assertEquals(ccf4.getFactorToEur(), dtoCurrencyConversionFactor.getFactorToEur());
		Assertions.assertEquals(ccf4.getInputDate(), dtoCurrencyConversionFactor.getInputDate());
	}
}