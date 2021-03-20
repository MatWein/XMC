package org.xmc.fe.stages.main.logging;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.xmc.fe.ui.FxmlController;

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
