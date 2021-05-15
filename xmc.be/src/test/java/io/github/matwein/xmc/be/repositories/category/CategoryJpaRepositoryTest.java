package io.github.matwein.xmc.be.repositories.category;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.cashaccount.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

class CategoryJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private CategoryJpaRepository repository;
	
	@Test
	void testFindById() {
		graphGenerator.createCategory();
		Category category = graphGenerator.createCategory();
		graphGenerator.createCategory();
		
		flushAndClear();
		
		Optional<Category> result = repository.findById(category.getId());
		
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(category, result.get());
	}
}