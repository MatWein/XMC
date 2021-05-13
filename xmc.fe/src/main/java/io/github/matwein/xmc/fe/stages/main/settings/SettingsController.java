package io.github.matwein.xmc.fe.stages.main.settings;

import io.github.matwein.xmc.fe.ui.FxmlComponentFactory;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.tuple.Pair;

@FxmlController
public class SettingsController {
	@FXML private VBox settingContentArea;
	
	@FXML
	public void onImportTemplates() {
		switchContent(FxmlKey.SETTINGS_IMPORT_TEMPLATES);
	}
	
	@FXML
	public void onAnalyseFavourites() {
		switchContent(FxmlKey.SETTINGS_ANALYSE_FAVOURITES);
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
