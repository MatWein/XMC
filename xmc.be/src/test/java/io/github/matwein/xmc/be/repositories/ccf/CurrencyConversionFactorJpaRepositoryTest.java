package io.github.matwein.xmc.be.repositories.ccf;

import com.google.common.collect.Sets;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.CurrencyConversionFactor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

class CurrencyConversionFactorJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private CurrencyConversionFactorJpaRepository repository;
	
	@Test
	void testFindByCurrencyIn() {
		CurrencyConversionFactor ccf1 = graphGenerator.createCurrencyConversionFactor("USD", LocalDateTime.now(), BigDecimal.valueOf(0.83));
		graphGenerator.createCurrencyConversionFactor("AUD", LocalDateTime.now(), BigDecimal.valueOf(0.55));
		CurrencyConversionFactor ccf3 = graphGenerator.createCurrencyConversionFactor("JPY", LocalDateTime.now(), BigDecimal.valueOf(0.91));
		
		flushAndClear();
		
		List<CurrencyConversionFactor> result = repository.findByCurrencyIn(Sets.newHashSet("USD", "JPY"));
		
		Assertions.assertEquals(Sets.newHashSet(ccf1, ccf3), Sets.newHashSet(result));
	}
}