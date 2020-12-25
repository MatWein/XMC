package org.xmc.be.repositories.settings;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;

class SettingsJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private SettingsJpaRepository repository;
	
	@Test
	void testFindByType() {
		throw new RuntimeException("implement me");
	}
}