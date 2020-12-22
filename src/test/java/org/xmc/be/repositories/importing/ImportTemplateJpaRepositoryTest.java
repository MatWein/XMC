package org.xmc.be.repositories.importing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;

class ImportTemplateJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private ImportTemplateJpaRepository repository;
	
	@Test
	void findByTypeAndName() {
		throw new RuntimeException("implement me");
	}
}