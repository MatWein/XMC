package io.github.matwein.xmc.be.repositories.stock;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.Stock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

class StockJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private StockJpaRepository repository;
	
	@Test
	void testFindById() {
		graphGenerator.createStock();
		Stock stock2 = graphGenerator.createStock();
		graphGenerator.createStock();
		
		flushAndClear();
		
		Optional<Stock> result = repository.findById(stock2.getId());
		
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(stock2, result.get());
	}
	
	@Test
	void testFindById_NotFound() {
		flushAndClear();
		
		Optional<Stock> result = repository.findById(20L);
		
		Assertions.assertTrue(result.isEmpty());
	}
}