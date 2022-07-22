package io.github.matwein.xmc.fe.stages.main.settings.content;

import io.github.matwein.xmc.common.services.settings.ISettingsService;
import io.github.matwein.xmc.common.stubs.settings.SettingType;
import io.github.matwein.xmc.fe.async.AsyncMonitor;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.ui.FxmlController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@FxmlController
public class SettingsExtrasController {
	private final AsyncProcessor asyncProcessor;
	private final ISettingsService settingsService;
	
	@FXML private CheckBox showSnowCheckbox;
	@FXML private VBox extrasRoot;
	
	@Autowired
	public SettingsExtrasController(
			AsyncProcessor asyncProcessor,
			ISettingsService settingsService) {
		
		this.asyncProcessor = asyncProcessor;
		this.settingsService = settingsService;
	}
	
	@FXML
	public void initialize() {
		asyncProcessor.runAsync(
				() -> extrasRoot.setDisable(true),
				this::loadSettings,
				this::applySettings,
				() -> extrasRoot.setDisable(false)
		);
	}
	
	private void applySettings(Map<SettingType, Serializable> settings) {
		showSnowCheckbox.setSelected(Boolean.TRUE.equals(settings.get(SettingType.EXTRAS_SHOW_SNOW)));
		showSnowCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> asyncProcessor.runAsyncVoid(
				monitor -> settingsService.saveSetting(monitor, SettingType.EXTRAS_SHOW_SNOW, newValue)
		));
	}
	
	private Map<SettingType, Serializable> loadSettings(AsyncMonitor monitor) {
		Map<SettingType, Serializable> settings = new HashMap<>();
		
		settings.put(SettingType.EXTRAS_SHOW_SNOW, settingsService.loadSetting(monitor, SettingType.EXTRAS_SHOW_SNOW));
		
		return settings;
	}
}
