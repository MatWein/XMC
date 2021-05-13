package io.github.matwein.xmc.be.services.settings;

import io.github.matwein.xmc.be.entities.settings.Setting;
import io.github.matwein.xmc.be.repositories.settings.SettingsJpaRepository;
import io.github.matwein.xmc.be.services.settings.controller.SettingValueCaster;
import io.github.matwein.xmc.common.services.settings.ISettingsService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.settings.SettingType;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

@Service
@Transactional
public class SettingsService implements ISettingsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SettingsService.class);
	
	private final SettingValueCaster settingValueCaster;
	private final SettingsJpaRepository settingsJpaRepository;
	
	@Autowired
	public SettingsService(
			SettingValueCaster settingValueCaster,
			SettingsJpaRepository settingsJpaRepository) {
		
		this.settingValueCaster = settingValueCaster;
		this.settingsJpaRepository = settingsJpaRepository;
	}
	
	public <T extends Serializable> T loadSetting(IAsyncMonitor monitor, SettingType settingType) {
		LOGGER.info("Loading setting for type '{}'.", settingType);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_SETTING));
		
		Optional<Setting> setting = settingsJpaRepository.findByType(settingType);
		
		T savedValue = settingValueCaster.castToType(settingType, setting.map(Setting::getValue).orElse(null));
		T result = Optional.ofNullable(savedValue).orElse(settingType.getDefaultValue());
		
		LOGGER.info("Got setting value '{}'.", result);
		return result;
	}
	
	@Override
	public void saveSetting(IAsyncMonitor monitor, SettingType settingType, Serializable valueToSave) {
		LOGGER.info("Saving setting for type '{}' with value '{}'.", settingType, valueToSave);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_SETTING));
		
		String valueAsString = StringUtils.defaultString(settingValueCaster.castToString(settingType, valueToSave));
		
		Optional<Setting> setting = settingsJpaRepository.findByType(settingType);
		
		if (setting.isPresent()) {
			setting.get().setValue(valueAsString);
			settingsJpaRepository.save(setting.get());
		} else {
			Setting newSetting = new Setting();
			newSetting.setType(settingType);
			newSetting.setValue(valueAsString);
			settingsJpaRepository.save(newSetting);
		}
	}
}
