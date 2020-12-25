package org.xmc.be.repositories.settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.be.entities.settings.Setting;
import org.xmc.be.entities.settings.SettingType;

import java.util.Optional;

class SettingsJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private SettingsJpaRepository repository;
	
	@Test
	void testFindByType() {
		Setting setting = graphGenerator.createSetting(SettingType.EXTRAS_SHOW_SNOW, Boolean.TRUE.toString());
		
		flushAndClear();
		
		Optional<Setting> result = repository.findByType(SettingType.EXTRAS_SHOW_SNOW);
		
		Assertions.assertEquals(setting, result.get());
	}
	
	@Test
	void testFindByType_NotFound() {
		Optional<Setting> result = repository.findByType(SettingType.EXTRAS_SHOW_SNOW);
		
		Assertions.assertEquals(Optional.empty(), result);
	}
}