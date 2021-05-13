package io.github.matwein.xmc.common.services.settings;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.settings.SettingType;

import java.io.Serializable;

public interface ISettingsService {
	<T extends Serializable> T loadSetting(IAsyncMonitor monitor, SettingType settingType);
	
	void saveSetting(IAsyncMonitor monitor, SettingType settingType, Serializable valueToSave);
}
