package io.github.matwein.xmc.be.repositories.category;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.StockCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

class StockCategoryJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private StockCategoryJpaRepository repository;
	
	@Test
	void testFindById() {
		graphGenerator.createStockCategory();
		StockCategory stockCategory = graphGenerator.createStockCategory();
		graphGenerator.createStockCategory();
		
		Optional<StockCategory> result = repository.findById(stockCategory.getId());
		
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(stockCategory, result.get());
	}
}