package org.xmc.fe.stages.main.analysis;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.analysis.AnalysisAssetService;
import org.xmc.be.services.analysis.AnalysisChartCalculationService;
import org.xmc.be.services.analysis.TimeRangeService;
import org.xmc.common.stubs.analysis.AnalysisType;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAssetSelection;
import org.xmc.common.stubs.analysis.TimeRange;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.analysis.mapper.DtoAssetSelectionTreeItemMapper;
import org.xmc.fe.stages.main.analysis.mapper.XYChartSeriesMapper;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationDatePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@FxmlController
public class AnalysisController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisController.class);
	
	private final TimeRangeService timeRangeService;
	private final AsyncProcessor asyncProcessor;
	private final AnalysisAssetService analysisAssetService;
	private final DtoAssetSelectionTreeItemMapper dtoAssetSelectionTreeItemMapper;
	private final AnalysisChartCalculationService analysisChartCalculationService;
	private final XYChartSeriesMapper xyChartSeriesMapper;
	
	@FXML private MenuButton favouriteMenuButton;
	@FXML private ValidationComboBox<AnalysisType> analysisTypeComboBox;
	@FXML private TreeView<DtoAssetSelection> selectedAssetsTreeView;
	@FXML private ValidationComboBox<TimeRange> timeRangeComboBox;
	@FXML private ValidationDatePicker startDatePicker;
	@FXML private ValidationDatePicker endDatePicker;
	@FXML private VBox analysisInputVBox;
	@FXML private VBox analysisContentVBox;
	
	@Autowired
	public AnalysisController(
			TimeRangeService timeRangeService,
			AsyncProcessor asyncProcessor,
			AnalysisAssetService analysisAssetService,
			DtoAssetSelectionTreeItemMapper dtoAssetSelectionTreeItemMapper,
			AnalysisChartCalculationService analysisChartCalculationService,
			XYChartSeriesMapper xyChartSeriesMapper) {
		
		this.timeRangeService = timeRangeService;
		this.asyncProcessor = asyncProcessor;
		this.analysisAssetService = analysisAssetService;
		this.dtoAssetSelectionTreeItemMapper = dtoAssetSelectionTreeItemMapper;
		this.analysisChartCalculationService = analysisChartCalculationService;
		this.xyChartSeriesMapper = xyChartSeriesMapper;
	}
	
	@FXML
	public void initialize() {
		analysisTypeComboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(MessageKey.ANALYSIS_TYPE, t)));
		
		timeRangeComboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(MessageKey.TIME_RANGE_TYPE, t)));
		timeRangeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> onTimeRangeSelected(newValue));
		
		startDatePicker.disableProperty().bind(timeRangeComboBox.valueProperty().isNotEqualTo(TimeRange.USER_DEFINED));
		startDatePicker.setValue(LocalDate.now().minusMonths(12));
		
		endDatePicker.disableProperty().bind(timeRangeComboBox.valueProperty().isNotEqualTo(TimeRange.USER_DEFINED));
		
		showMessagePane(MessageKey.ANALYSIS_HINT);
		
		selectedAssetsTreeView.setCellFactory(CheckBoxTreeCell.forTreeView());
		
		asyncProcessor.runAsync(
				analysisAssetService::loadAssets,
				result -> {
					CheckBoxTreeItem<DtoAssetSelection> rootNode = dtoAssetSelectionTreeItemMapper.map(result, () -> onTimeRangeSelected(timeRangeComboBox.getValue()));
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
		asyncProcessor.runAsync(
				this::beforeCalculation,
				this::calculateChartForSelectedType,
				this::showResultChart,
				this::afterCalculation
		);
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
	
	private <T> Optional<T> calculateChartForSelectedType(AsyncMonitor monitor) {
		AnalysisType analysisType = analysisTypeComboBox.getValue();
		if (analysisType == null) {
			return Optional.empty();
		}
		
		switch (analysisType) {
			case ABSOLUTE_ASSET_VALUE:
				return (Optional<T>)analysisChartCalculationService.calculateAbsoluteAssetValueLineChart(
						monitor,
						extractSelectedAssetIds(),
						startDatePicker.getValueOrNull(),
						endDatePicker.getValueOrNull());
			case AGGREGATED_ASSET_VALUE:
				return (Optional<T>)analysisChartCalculationService.calculateAggregatedAssetValueLineChart(
						monitor,
						extractSelectedAssetIds(),
						startDatePicker.getValueOrNull(),
						endDatePicker.getValueOrNull());
			case ABSOLUTE_AND_AGGREGATED_ASSET_VALUE:
				return (Optional<T>)analysisChartCalculationService.calculateAbsoluteAndAggregatedAssetValueLineChart(
						monitor,
						extractSelectedAssetIds(),
						startDatePicker.getValueOrNull(),
						endDatePicker.getValueOrNull());
			default:
				String message = String.format("Could not calculate chart for unknown analysis type '%s'.", analysisType);
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
	
	private <T> void showResultChart(Optional<T> result) {
		if (result.isEmpty()) {
			showMessagePane(MessageKey.ANALYSIS_NO_CALCULATION_RESULT);
			return;
		}
		
		analysisContentVBox.getChildren().clear();
		Node chart = createChart(result.get());
		VBox.setVgrow(chart, Priority.ALWAYS);
		analysisContentVBox.getChildren().add(chart);
	}
	
	private <T> Node createChart(T result) {
		AnalysisType analysisType = analysisTypeComboBox.getValue();
		
		switch (analysisType) {
			case ABSOLUTE_ASSET_VALUE:
			case AGGREGATED_ASSET_VALUE:
			case ABSOLUTE_AND_AGGREGATED_ASSET_VALUE:
				CategoryAxis xAxis = new CategoryAxis();
				xAxis.setLabel(MessageAdapter.getByKey(MessageKey.ANALYSIS_AXIS_DATE));
				
				NumberAxis yAxis = new NumberAxis();
				yAxis.setLabel(MessageAdapter.getByKey(MessageKey.ANALYSIS_AXIS_VALUE_IN_EUR));
				
				LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
				lineChart.setTitle(MessageAdapter.getByKey(MessageKey.ANALYSIS_TYPE, analysisType));
				
				List<DtoChartSeries<LocalDateTime, Number>> series = (List<DtoChartSeries<LocalDateTime, Number>>)result;
				List<XYChart.Series<String, Number>> mappedSeries = xyChartSeriesMapper.mapAll(series);
				
				lineChart.getStyleClass().add("line-chart");
				lineChart.getData().addAll(mappedSeries);
				
				return lineChart;
			default:
				String message = String.format("Could not show chart for unknown analysis type '%s'.", analysisType);
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
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
				monitor -> timeRangeService.calculateStartAndEndDate(monitor, newValue, extractSelectedAssetIds()),
				result -> {
					startDatePicker.setValue(result.getLeft());
					endDatePicker.setValue(result.getRight());
				}
		);
	}
	
	private Multimap<AssetType, Long> extractSelectedAssetIds() {
		CheckBoxTreeItem<DtoAssetSelection> root = (CheckBoxTreeItem<DtoAssetSelection>)selectedAssetsTreeView.getRoot();
		
		Multimap<AssetType, Long> result = ArrayListMultimap.create();
		populateSelectedAssets(result, root);
		return result;
	}
	
	private void populateSelectedAssets(Multimap<AssetType, Long> result, CheckBoxTreeItem<DtoAssetSelection> node) {
		if (node.getValue().getId() != null && node.isSelected()) {
			result.put(node.getValue().getAssetType(), node.getValue().getId());
		}
		
		for (TreeItem<DtoAssetSelection> child : node.getChildren()) {
			populateSelectedAssets(result, (CheckBoxTreeItem<DtoAssetSelection>)child);
		}
	}
}
