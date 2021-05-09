package org.xmc.fe.stages.main.analysis;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.analysis.AnalysisAssetService;
import org.xmc.be.services.analysis.AnalysisFavouriteService;
import org.xmc.be.services.analysis.TimeRangeService;
import org.xmc.common.stubs.analysis.AnalysisType;
import org.xmc.common.stubs.analysis.DtoAnalysisFavourite;
import org.xmc.common.stubs.analysis.DtoAssetSelection;
import org.xmc.common.stubs.analysis.TimeRange;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.analysis.logic.*;
import org.xmc.fe.stages.main.analysis.mapper.DtoAssetSelectionTreeItemMapper;
import org.xmc.fe.ui.DialogHelper;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationDatePicker;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@FxmlController
public class AnalysisContentController {
	private final TimeRangeService timeRangeService;
	private final AsyncProcessor asyncProcessor;
	private final AnalysisAssetService analysisAssetService;
	private final DtoAssetSelectionTreeItemMapper dtoAssetSelectionTreeItemMapper;
	private final ChartNodeFactory chartNodeFactory;
	private final ChartDataForSelectedTypeLoadingController chartDataForSelectedTypeLoadingController;
	private final SelectedAssetIdsExtractor selectedAssetIdsExtractor;
	private final AnalysisFavouriteService analysisFavouriteService;
	private final AssetIdsSelector assetIdsSelector;
	private final AnalysisAllFavouritesRefreshController analysisAllFavouritesRefreshController;
	
	@FXML private MenuButton favouriteMenuButton;
	@FXML private ValidationComboBox<AnalysisType> analysisTypeComboBox;
	@FXML private TreeView<DtoAssetSelection> selectedAssetsTreeView;
	@FXML private ValidationComboBox<TimeRange> timeRangeComboBox;
	@FXML private ValidationDatePicker startDatePicker;
	@FXML private ValidationDatePicker endDatePicker;
	@FXML private VBox analysisInputVBox;
	@FXML private VBox analysisContentVBox;
	
	@Autowired
	public AnalysisContentController(
			TimeRangeService timeRangeService,
			AsyncProcessor asyncProcessor,
			AnalysisAssetService analysisAssetService,
			DtoAssetSelectionTreeItemMapper dtoAssetSelectionTreeItemMapper,
			ChartNodeFactory chartNodeFactory,
			ChartDataForSelectedTypeLoadingController chartDataForSelectedTypeLoadingController,
			SelectedAssetIdsExtractor selectedAssetIdsExtractor,
			AnalysisFavouriteService analysisFavouriteService,
			AssetIdsSelector assetIdsSelector,
			AnalysisAllFavouritesRefreshController analysisAllFavouritesRefreshController) {
		
		this.timeRangeService = timeRangeService;
		this.asyncProcessor = asyncProcessor;
		this.analysisAssetService = analysisAssetService;
		this.dtoAssetSelectionTreeItemMapper = dtoAssetSelectionTreeItemMapper;
		this.chartNodeFactory = chartNodeFactory;
		this.chartDataForSelectedTypeLoadingController = chartDataForSelectedTypeLoadingController;
		this.selectedAssetIdsExtractor = selectedAssetIdsExtractor;
		this.analysisFavouriteService = analysisFavouriteService;
		this.assetIdsSelector = assetIdsSelector;
		this.analysisAllFavouritesRefreshController = analysisAllFavouritesRefreshController;
	}
	
	@FXML
	public void initialize() {
		favouriteMenuButton.setUserData(this);
		
		analysisTypeComboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(MessageKey.ANALYSIS_TYPE, t)));
		analysisTypeComboBox.setValue(AnalysisType.ABSOLUTE_AND_AGGREGATED_ASSET_VALUE);
		
		timeRangeComboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(MessageKey.TIME_RANGE_TYPE, t)));
		timeRangeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> onTimeRangeSelected(newValue));
		
		startDatePicker.disableProperty().bind(timeRangeComboBox.valueProperty().isNotEqualTo(TimeRange.USER_DEFINED));
		startDatePicker.setValue(LocalDate.now().minusMonths(12));
		
		endDatePicker.disableProperty().bind(timeRangeComboBox.valueProperty().isNotEqualTo(TimeRange.USER_DEFINED));
		
		showMessagePane(MessageKey.ANALYSIS_HINT);
		
		selectedAssetsTreeView.setCellFactory(CheckBoxTreeCell.forTreeView());
		timeRangeComboBox.setValue(TimeRange.LAST_YEAR);
		
		refreshAssetTree();
		refreshFavourites();
	}
	
	@FXML
	public void clearInput() {
		analysisTypeComboBox.setValue(AnalysisType.ABSOLUTE_AND_AGGREGATED_ASSET_VALUE);
		
		refreshAssetTree();
		
		timeRangeComboBox.setValue(TimeRange.LAST_YEAR);
		
		onCalculate();
	}
	
	@FXML
	public void saveAsFavourite() {
		Optional<String> favouriteName = DialogHelper.showTextInputDialog(MessageKey.ANALYSIS_SAVE_FAVOURITE_NAME);
		
		if (favouriteName.isPresent()) {
			var analysisToSave = new DtoAnalysisFavourite();
			
			analysisToSave.setName(favouriteName.get());
			analysisToSave.setAnalysisType(analysisTypeComboBox.getValue());
			analysisToSave.setTimeRange(timeRangeComboBox.getValue());
			analysisToSave.setAssetIds(selectedAssetIdsExtractor.extractSelectedAssetIds(selectedAssetsTreeView));
			analysisToSave.setStartDate(startDatePicker.getValueOrNull());
			analysisToSave.setEndDate(endDatePicker.getValueOrNull());
			
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> analysisFavouriteService.saveOrUpdateAnalysisFavourite(monitor, analysisToSave),
					analysisAllFavouritesRefreshController::refreshAllFavourites
			);
		}
	}
	
	@FXML
	public void onCalculate() {
		asyncProcessor.runAsync(
				this::beforeCalculation,
				monitor -> chartDataForSelectedTypeLoadingController.calculateChartForSelectedType(
						monitor,
						analysisTypeComboBox.getValue(),
						selectedAssetIdsExtractor.extractSelectedAssetIds(selectedAssetsTreeView),
						startDatePicker.getValueOrNull(),
						endDatePicker.getValueOrNull()),
				this::showResultChart,
				this::afterCalculation
		);
	}
	
	private void refreshAssetTree() {
		asyncProcessor.runAsync(
				analysisAssetService::loadAssets,
				this::updateAssetTree
		);
	}
	
	public void updateAssetTree(DtoAssetSelection result) {
		var rootNode = dtoAssetSelectionTreeItemMapper.map(result, () -> onTimeRangeSelected(timeRangeComboBox.getValue()));
		selectedAssetsTreeView.setRoot(rootNode);
	}
	
	private void onShowSavedFavourite(DtoAnalysisFavourite favourite) {
		analysisTypeComboBox.setValue(favourite.getAnalysisType());
		timeRangeComboBox.setValue(favourite.getTimeRange());
		startDatePicker.setValue(favourite.getStartDate());
		endDatePicker.setValue(favourite.getEndDate());
		
		assetIdsSelector.selectAssetsById(selectedAssetsTreeView, favourite.getAssetIds());
		
		onCalculate();
	}
	
	private void refreshFavourites() {
		asyncProcessor.runAsync(
				analysisFavouriteService::loadAnalyseFavourites,
				this::updateFavouriteMenuButton
		);
	}
	
	public void updateFavouriteMenuButton(List<DtoAnalysisFavourite> result) {
		favouriteMenuButton.getItems().clear();
		
		for (DtoAnalysisFavourite favourite : result) {
			MenuItem menuItem = new MenuItem(favourite.getName());
			menuItem.setOnAction(event -> onShowSavedFavourite(favourite));
			favouriteMenuButton.getItems().add(menuItem);
		}
		
		favouriteMenuButton.setDisable(favouriteMenuButton.getItems().size() == 0);
	}
	
	private void beforeCalculation() {
		analysisInputVBox.setDisable(true);
		showLoadingSpinner();
	}
	
	private void showLoadingSpinner() {
		analysisContentVBox.getChildren().clear();
		VBox spinnerPane = FxmlComponentFactory.loadWithoutController(FxmlKey.FRAGMENT_SPINNER_PANE);
		VBox.setVgrow(spinnerPane, Priority.ALWAYS);
		analysisContentVBox.getChildren().add(spinnerPane);
	}
	
	private <T> void showResultChart(Optional<T> result) {
		if (result.isEmpty() || (result.get() instanceof Collection && ((Collection<?>) result.get()).isEmpty())) {
			showMessagePane(MessageKey.ANALYSIS_NO_CALCULATION_RESULT);
			return;
		}
		
		analysisContentVBox.getChildren().clear();
		Node chart = chartNodeFactory.createChart(result.get(), analysisTypeComboBox.getValue());
		VBox.setVgrow(chart, Priority.ALWAYS);
		analysisContentVBox.getChildren().add(chart);
	}
	
	private void showMessagePane(MessageKey messageKey) {
		analysisContentVBox.getChildren().clear();
	
		VBox messagePane = FxmlComponentFactory.loadWithoutController(FxmlKey.FRAGMENT_MESSAGE_PANE);
		VBox.setVgrow(messagePane, Priority.ALWAYS);
		((Label)messagePane.lookup(".label")).setText(MessageAdapter.getByKey(messageKey));
	
		analysisContentVBox.getChildren().add(messagePane);
	}
	
	private void afterCalculation() {
		analysisInputVBox.setDisable(false);
	}
	
	private void onTimeRangeSelected(TimeRange newValue) {
		if (newValue == TimeRange.USER_DEFINED) {
			return;
		}
		
		asyncProcessor.runAsync(
				monitor -> timeRangeService.calculateStartAndEndDate(
						monitor,
						newValue,
						selectedAssetIdsExtractor.extractSelectedAssetIds(selectedAssetsTreeView)),
				
				result -> {
					startDatePicker.setValue(result.getLeft());
					endDatePicker.setValue(result.getRight());
				}
		);
	}
}
