package org.xmc.fe.stages.main.analysis;

import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TreeView;
import org.xmc.common.stubs.analysis.AnalysisType;
import org.xmc.common.stubs.analysis.TimeRange;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationDatePicker;

@FxmlController
public class AnalysisController {
	@FXML private MenuButton favouriteMenuButton;
	@FXML private ValidationComboBox<AnalysisType> analysisTypeComboBox;
	@FXML private TreeView<?> selectedAssetsTreeView;
	@FXML private ValidationComboBox<TimeRange> timeRangeComboBox;
	@FXML private ValidationDatePicker startDatePicker;
	@FXML private ValidationDatePicker endDatePicker;
	
	@FXML
	public void initialize() {
		analysisTypeComboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(MessageKey.ANALYSIS_TYPE, t)));
		timeRangeComboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(MessageKey.TIME_RANGE_TYPE, t)));
	}
	
	@FXML
	public void clearInput() {
	}
	
	@FXML
	public void saveAsFavourite() {
	}
	
	@FXML
	public void onCalculate() {
	}
}
