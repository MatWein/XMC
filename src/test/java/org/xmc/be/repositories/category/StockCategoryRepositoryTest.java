package org.xmc.be.repositories.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;

class StockCategoryRepositoryTest extends IntegrationTest {
	@Autowired
	private StockCategoryRepository repository;
	
	@Test
	void testLoadOverview() {
		throw new RuntimeException("implement me");
	}
}