package io.github.matwein.xmc.fe.stages.main.analysis.logic;

import io.github.matwein.xmc.common.stubs.analysis.DtoAnalysisFavourite;
import io.github.matwein.xmc.fe.ui.charts.ExtendedBarChart;
import io.github.matwein.xmc.fe.ui.charts.ExtendedLineChart;
import io.github.matwein.xmc.fe.ui.charts.ExtendedPieChart;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AnalysisDashboardNodeFactory {
	private final ChartNodeFactory chartNodeFactory;
	
	@Autowired
	public AnalysisDashboardNodeFactory(ChartNodeFactory chartNodeFactory) {
		this.chartNodeFactory = chartNodeFactory;
	}
	
	public Node createNode(DtoAnalysisFavourite dtoAnalysisFavourite, Optional<Object> data) {
		if (data.isPresent()) {
			return createAndManipulateNode(dtoAnalysisFavourite, data.get());
		} else {
			return createHintLabel();
		}
	}
	
	private Label createHintLabel() {
		Label label = new Label(MessageAdapter.getByKey(MessageKey.ANALYSIS_NO_CALCULATION_RESULT));
		label.setTextAlignment(TextAlignment.CENTER);
		label.setAlignment(Pos.CENTER);
		
		return label;
	}
	
	private Node createAndManipulateNode(DtoAnalysisFavourite dtoAnalysisFavourite, Object data) {
		Node chart = chartNodeFactory.createChart(data, dtoAnalysisFavourite.getAnalysisType());
		
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
	}
}
