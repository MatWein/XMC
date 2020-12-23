package org.xmc.fe.stages.main.settings;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;

@FxmlController
public class SettingsController {
	@FXML private VBox settingContentArea;
	
	@FXML
	public void onImportTemplates() {
		switchContent(FxmlKey.SETTINGS_IMPORT_TEMPLATES);
	}
	
	@FXML
	public void onExtras() {
		switchContent(FxmlKey.SETTINGS_EXTRAS);
	}
	
	private void switchContent(FxmlKey fxmlKey) {
		Pair<Parent, Object> component = FxmlComponentFactory.load(fxmlKey);
		
		settingContentArea.getChildren().clear();
		settingContentArea.getChildren().add(component.getLeft());
	}
}
