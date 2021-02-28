package org.xmc.fe.stages.main.analysis;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TreeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.analysis.TimeRangeService;
import org.xmc.common.stubs.analysis.AnalysisType;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.TimeRange;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationDatePicker;

@FxmlController
public class AnalysisController {
	private final TimeRangeService timeRangeService;
	private final AsyncProcessor asyncProcessor;
	
	@FXML private MenuButton favouriteMenuButton;
	@FXML private ValidationComboBox<AnalysisType> analysisTypeComboBox;
	@FXML private TreeView<?> selectedAssetsTreeView;
	@FXML private ValidationComboBox<TimeRange> timeRangeComboBox;
	@FXML private ValidationDatePicker startDatePicker;
	@FXML private ValidationDatePicker endDatePicker;
	
	@Autowired
	public AnalysisController(TimeRangeService timeRangeService, AsyncProcessor asyncProcessor) {
		this.timeRangeService = timeRangeService;
		this.asyncProcessor = asyncProcessor;
	}
	
	@FXML
	public void initialize() {
		analysisTypeComboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(MessageKey.ANALYSIS_TYPE, t)));
		
		timeRangeComboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(MessageKey.TIME_RANGE_TYPE, t)));
		timeRangeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> onTimeRangeSelected(newValue));
		
		startDatePicker.disableProperty().bind(timeRangeComboBox.valueProperty().isNotEqualTo(TimeRange.USER_DEFINED));
		endDatePicker.disableProperty().bind(timeRangeComboBox.valueProperty().isNotEqualTo(TimeRange.USER_DEFINED));
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
	
	private Multimap<AssetType, Long> extractSelectedAssetIds() {
		return ArrayListMultimap.create();
	}
	
	private void onTimeRangeSelected(TimeRange newValue) {
		if (newValue == TimeRange.USER_DEFINED) {
			return;
		}
		
		asyncProcessor.runAsync(
				monitor -> timeRangeService.calculateStartAndEndDate(monitor, newValue, extractSelectedAssetIds()),
				result -> {
					startDatePicker.setValue(result.getLeft());
					endDatePicker.setValue(result.getRight());
				}
		);
	}
}
