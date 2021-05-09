package io.github.matwein.xmc.fe.stages.main.analysis;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.matwein.xmc.be.services.analysis.AnalysisFavouriteService;
import io.github.matwein.xmc.be.services.analysis.TimeRangeService;
import io.github.matwein.xmc.common.stubs.analysis.DtoAnalysisFavourite;
import io.github.matwein.xmc.common.stubs.analysis.TimeRange;
import io.github.matwein.xmc.fe.async.AsyncMonitor;
import io.github.matwein.xmc.fe.stages.main.analysis.logic.AnalysisDashboardNodeFactory;
import io.github.matwein.xmc.fe.stages.main.analysis.logic.ChartDataForSelectedTypeLoadingController;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.MessageAdapter;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.dashboard.DashboardContentTile;
import io.github.matwein.xmc.fe.ui.dashboard.DtoDashboardTile;
import io.github.matwein.xmc.fe.ui.dashboard.IDashboardTileController;

import java.time.LocalDate;
import java.util.Optional;

@FxmlController
public class AnalysisDashboardTileController implements IDashboardTileController {
	private static final int STEP_COUNT = 3;
	
	private final AnalysisFavouriteService analysisFavouriteService;
	private final ChartDataForSelectedTypeLoadingController chartDataForSelectedTypeLoadingController;
	private final TimeRangeService timeRangeService;
	private final AnalysisDashboardNodeFactory analysisDashboardNodeFactory;
	
	@FXML private AnchorPane analysisDashboardTileAnchorPane;
	
	@Autowired
	public AnalysisDashboardTileController(
			AnalysisFavouriteService analysisFavouriteService,
			ChartDataForSelectedTypeLoadingController chartDataForSelectedTypeLoadingController,
			TimeRangeService timeRangeService,
			AnalysisDashboardNodeFactory analysisDashboardNodeFactory) {
		
		this.analysisFavouriteService = analysisFavouriteService;
		this.chartDataForSelectedTypeLoadingController = chartDataForSelectedTypeLoadingController;
		this.timeRangeService = timeRangeService;
		this.analysisDashboardNodeFactory = analysisDashboardNodeFactory;
	}
	
	@Override
	public boolean loadAndApplyData(AsyncMonitor monitor, DtoDashboardTile tile, DashboardContentTile contentTile) {
		long analysisFavouriteId = tile.getData();
		
		monitor.setProgressByItemCount(0, STEP_COUNT);
		
		Optional<DtoAnalysisFavourite> dtoAnalysisFavourite = analysisFavouriteService.loadAnalyseFavourite(monitor, analysisFavouriteId);
		if (dtoAnalysisFavourite.isEmpty()) {
			return false;
		}
		
		monitor.setProgressByItemCount(1, STEP_COUNT);
		
		Pair<LocalDate, LocalDate> recalculatedStartAndEndDate = recalculateStartAndEndDate(monitor, dtoAnalysisFavourite.get());
		
		monitor.setProgressByItemCount(2, STEP_COUNT);
		
		Optional<Object> data = chartDataForSelectedTypeLoadingController.calculateChartForSelectedType(
				monitor,
				dtoAnalysisFavourite.get().getAnalysisType(),
				dtoAnalysisFavourite.get().getAssetIds(),
				recalculatedStartAndEndDate.getLeft(),
				recalculatedStartAndEndDate.getRight());
		
		monitor.setProgressByItemCount(3, STEP_COUNT);
		
		Platform.runLater(() -> {
			Node node = analysisDashboardNodeFactory.createNode(dtoAnalysisFavourite.get(), data);
			
			AnchorPane.setBottomAnchor(node, 0.0);
			AnchorPane.setLeftAnchor(node, 0.0);
			AnchorPane.setRightAnchor(node, 0.0);
			AnchorPane.setTopAnchor(node, 0.0);
			
			contentTile.setTitle(MessageAdapter.getByKey(MessageKey.ANALYSIS_TYPE, dtoAnalysisFavourite.get().getAnalysisType()));
			analysisDashboardTileAnchorPane.getChildren().add(node);
		});
		
		return true;
	}
	
	private Pair<LocalDate, LocalDate> recalculateStartAndEndDate(AsyncMonitor monitor, DtoAnalysisFavourite dtoAnalysisFavourite) {
		if (dtoAnalysisFavourite.getTimeRange() == TimeRange.USER_DEFINED) {
			return ImmutablePair.of(dtoAnalysisFavourite.getStartDate(), dtoAnalysisFavourite.getEndDate());
		} else {
			return timeRangeService.calculateStartAndEndDate(
					monitor,
					dtoAnalysisFavourite.getTimeRange(),
					dtoAnalysisFavourite.getAssetIds());
		}
	}
}
