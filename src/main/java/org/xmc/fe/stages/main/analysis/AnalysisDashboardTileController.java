package org.xmc.fe.stages.main.analysis;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.analysis.AnalysisFavouriteService;
import org.xmc.common.stubs.analysis.DtoAnalysisFavourite;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.stages.main.analysis.logic.ChartDataForSelectedTypeLoadingController;
import org.xmc.fe.stages.main.analysis.logic.ChartNodeFactory;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.charts.ExtendedBarChart;
import org.xmc.fe.ui.charts.ExtendedLineChart;
import org.xmc.fe.ui.charts.ExtendedPieChart;
import org.xmc.fe.ui.dashboard.DashboardContentTile;
import org.xmc.fe.ui.dashboard.DtoDashboardTile;
import org.xmc.fe.ui.dashboard.IDashboardTileController;

import java.util.Optional;

@FxmlController
public class AnalysisDashboardTileController implements IDashboardTileController {
	private final AnalysisFavouriteService analysisFavouriteService;
	private final ChartDataForSelectedTypeLoadingController chartDataForSelectedTypeLoadingController;
	private final ChartNodeFactory chartNodeFactory;
	
	@FXML private AnchorPane analysisDashboardTileAnchorPane;
	
	@Autowired
	public AnalysisDashboardTileController(
			AnalysisFavouriteService analysisFavouriteService,
			ChartDataForSelectedTypeLoadingController chartDataForSelectedTypeLoadingController,
			ChartNodeFactory chartNodeFactory) {
		
		this.analysisFavouriteService = analysisFavouriteService;
		this.chartDataForSelectedTypeLoadingController = chartDataForSelectedTypeLoadingController;
		this.chartNodeFactory = chartNodeFactory;
	}
	
	@Override
	public boolean loadAndApplyData(AsyncMonitor monitor, DtoDashboardTile tile, DashboardContentTile contentTile) {
		long analysisFavouriteId = (Long)tile.getData();
		
		Optional<DtoAnalysisFavourite> dtoAnalysisFavourite = analysisFavouriteService.loadAnalyseFavourite(monitor, analysisFavouriteId);
		if (dtoAnalysisFavourite.isEmpty()) {
			return false;
		}
		
		Optional<Object> data = chartDataForSelectedTypeLoadingController.calculateChartForSelectedType(
				monitor,
				dtoAnalysisFavourite.get().getAnalysisType(),
				dtoAnalysisFavourite.get().getAssetIds(),
				dtoAnalysisFavourite.get().getStartDate(),
				dtoAnalysisFavourite.get().getEndDate());
		
		Platform.runLater(() -> {
			Node node = createNode(dtoAnalysisFavourite.get(), data);
			
			AnchorPane.setBottomAnchor(node, 0.0);
			AnchorPane.setLeftAnchor(node, 0.0);
			AnchorPane.setRightAnchor(node, 0.0);
			AnchorPane.setTopAnchor(node, 0.0);
			
			contentTile.setTitle(MessageAdapter.getByKey(MessageKey.ANALYSIS_TYPE, dtoAnalysisFavourite.get().getAnalysisType()));
			analysisDashboardTileAnchorPane.getChildren().add(node);
		});
		
		return true;
	}
	
	private Node createNode(DtoAnalysisFavourite dtoAnalysisFavourite, Optional<Object> data) {
		if (data.isPresent()) {
			Node chart = chartNodeFactory.createChart(data.get(), dtoAnalysisFavourite.getAnalysisType());
			
			if (chart instanceof ExtendedLineChart) {
				ExtendedLineChart chartNode = (ExtendedLineChart) chart;
				chartNode.setTitle(null);
				chartNode.setLegendVisible(false);
				chartNode.setMinHeight(0.0);
				chartNode.setMinWidth(0.0);
				chartNode.setShowHoverLabel(false);
			}
			
			if (chart instanceof ExtendedPieChart) {
				ExtendedPieChart chartNode = (ExtendedPieChart) chart;
				chartNode.setTitle(null);
				chartNode.setLegendVisible(false);
				chartNode.setMinHeight(0.0);
				chartNode.setMinWidth(0.0);
			}
			
			if (chart instanceof ExtendedBarChart) {
				ExtendedBarChart chartNode = (ExtendedBarChart) chart;
				chartNode.setTitle(null);
				chartNode.setLegendVisible(false);
				chartNode.setMinHeight(0.0);
				chartNode.setMinWidth(0.0);
			}
			
			return chart;
		} else {
			Label label = new Label(MessageAdapter.getByKey(MessageKey.ANALYSIS_NO_CALCULATION_RESULT));
			label.setTextAlignment(TextAlignment.CENTER);
			label.setAlignment(Pos.CENTER);
			
			return label;
		}
	}
}
