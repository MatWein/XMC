package io.github.matwein.xmc.be.repositories.settings;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.settings.Setting;
import io.github.matwein.xmc.common.stubs.settings.SettingType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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