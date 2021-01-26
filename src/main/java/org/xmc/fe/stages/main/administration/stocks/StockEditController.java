package org.xmc.fe.stages.main.administration.stocks;

import javafx.fxml.FXML;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationTextField;

@FxmlController
public class StockEditController {
	@FXML private ValidationTextField isinTextfield;
	@FXML private ValidationTextField wknTextfield;
	@FXML private ValidationTextField nameTextfield;
	@FXML private ValidationComboBox stockCategoryComboBox;
}
