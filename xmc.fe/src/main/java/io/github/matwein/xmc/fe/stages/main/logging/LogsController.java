package io.github.matwein.xmc.fe.stages.main.logging;

import io.github.matwein.xmc.fe.ui.FxmlController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

@FxmlController
public class LogsController {
	@FXML private TextArea textArea;
	
	@FXML
	public void initialize() {
		for (String line : LogsAppender.LOGS) {
			textArea.appendText(line);
		}
	}
}
