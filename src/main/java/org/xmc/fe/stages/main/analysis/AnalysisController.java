package org.xmc.fe.stages.main.analysis;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TreeItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.analysis.AnalysisAssetService;
import org.xmc.be.services.analysis.TimeRangeService;
import org.xmc.common.stubs.analysis.AnalysisType;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAssetSelection;
import org.xmc.common.stubs.analysis.TimeRange;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.analysis.mapper.DtoAssetSelectionTreeItemMapper;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.treetable.BaseTreeTable;
import org.xmc.fe.ui.components.treetable.TreeItemWithParams;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationDatePicker;

@FxmlController
public class AnalysisController {
	private final TimeRangeService timeRangeService;
	private final AsyncProcessor asyncProcessor;
	private final AnalysisAssetService analysisAssetService;
	private final DtoAssetSelectionTreeItemMapper dtoAssetSelectionTreeItemMapper;
	
	@FXML private MenuButton favouriteMenuButton;
	@FXML private ValidationComboBox<AnalysisType> analysisTypeComboBox;
	@FXML private BaseTreeTable<DtoAssetSelection> selectedAssetsTreeView;
	@FXML private ValidationComboBox<TimeRange> timeRangeComboBox;
	@FXML private ValidationDatePicker startDatePicker;
	@FXML private ValidationDatePicker endDatePicker;
	
	@Autowired
	public AnalysisController(
			TimeRangeService timeRangeService,
			AsyncProcessor asyncProcessor,
			AnalysisAssetService analysisAssetService,
			DtoAssetSelectionTreeItemMapper dtoAssetSelectionTreeItemMapper) {
		
		this.timeRangeService = timeRangeService;
		this.asyncProcessor = asyncProcessor;
		this.analysisAssetService = analysisAssetService;
		this.dtoAssetSelectionTreeItemMapper = dtoAssetSelectionTreeItemMapper;
	}
	
	@FXML
	public void initialize() {
		analysisTypeComboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(MessageKey.ANALYSIS_TYPE, t)));
		
		timeRangeComboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(MessageKey.TIME_RANGE_TYPE, t)));
		timeRangeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> onTimeRangeSelected(newValue));
		
		startDatePicker.disableProperty().bind(timeRangeComboBox.valueProperty().isNotEqualTo(TimeRange.USER_DEFINED));
		endDatePicker.disableProperty().bind(timeRangeComboBox.valueProperty().isNotEqualTo(TimeRange.USER_DEFINED));
		
		selectedAssetsTreeView.setOnTreeItemSelectionChanged(() -> onTimeRangeSelected(timeRangeComboBox.getValue()));
		
		asyncProcessor.runAsync(
				analysisAssetService::loadAssets,
				result -> {
					TreeItemWithParams<DtoAssetSelection, ?> rootNode = dtoAssetSelectionTreeItemMapper.map(result);
					selectedAssetsTreeView.setRoot(rootNode);
				}
		);
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
	
	private Multimap<AssetType, Long> extractSelectedAssetIds() {
		TreeItemWithParams<DtoAssetSelection, CheckBox> root = (TreeItemWithParams<DtoAssetSelection, CheckBox>)selectedAssetsTreeView.getRoot();
		
		Multimap<AssetType, Long> result = ArrayListMultimap.create();
		populateSelectedAssets(result, root);
		return result;
	}
	
	private void populateSelectedAssets(Multimap<AssetType, Long> result, TreeItemWithParams<DtoAssetSelection, CheckBox> node) {
		if (node.getValue().getId() != null && node.getParam().isSelected()) {
			result.put(node.getValue().getAssetType(), node.getValue().getId());
		}
		
		for (TreeItem<DtoAssetSelection> child : node.getChildren()) {
			populateSelectedAssets(result, (TreeItemWithParams<DtoAssetSelection, CheckBox>)child);
		}
	}
}
