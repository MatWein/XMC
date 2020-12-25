package org.xmc.be.repositories.settings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.settings.Setting;
import org.xmc.be.entities.settings.SettingType;

import java.util.Optional;

public interface SettingsJpaRepository extends JpaRepository<Setting, Long> {
	Optional<Setting> findByType(SettingType type);
}
