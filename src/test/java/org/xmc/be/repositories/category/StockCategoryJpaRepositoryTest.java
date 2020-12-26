package org.xmc.be.repositories.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;

class StockCategoryJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private StockCategoryJpaRepository repository;
	
	@Test
	void testFindByDeletionDateIsNull() {
		throw new RuntimeException("implement me");
	}
}