package io.github.matwein.xmc.be.repositories.settings;

import io.github.matwein.xmc.be.entities.settings.Setting;
import io.github.matwein.xmc.common.stubs.settings.SettingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingsJpaRepository extends JpaRepository<Setting, Long> {
	Optional<Setting> findByType(SettingType type);
}
