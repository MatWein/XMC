package io.github.matwein.xmc.be.repositories.ccf;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.CurrencyConversionFactor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class CurrencyConversionFactorJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private CurrencyConversionFactorJpaRepository repository;
	
	@Test
	void testFindByCurrencyIn() {
		CurrencyConversionFactor ccf1 = graphGenerator.createCurrencyConversionFactor("USD", LocalDateTime.now(), BigDecimal.valueOf(0.83));
		graphGenerator.createCurrencyConversionFactor("AUD", LocalDateTime.now(), BigDecimal.valueOf(0.55));
		CurrencyConversionFactor ccf3 = graphGenerator.createCurrencyConversionFactor("JPY", LocalDateTime.now(), BigDecimal.valueOf(0.91));
		
		flushAndClear();
		
		List<CurrencyConversionFactor> result = repository.findByCurrencyIn(Set.of("USD", "JPY"));
		
		Assertions.assertEquals(Set.of(ccf1, ccf3), new HashSet<>(result));
	}
}