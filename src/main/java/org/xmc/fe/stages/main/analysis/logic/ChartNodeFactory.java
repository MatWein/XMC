package org.xmc.fe.stages.main.analysis.logic;

import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.AnalysisType;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;

import java.util.List;

@Component
public class ChartNodeFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChartNodeFactory.class);
	
	private final IncomeOutgoingPieChartFactory incomeOutgoingPieChartFactory;
	private final TransactionsBarChartFactory transactionsBarChartFactory;
	private final AssetValueLineChartFactory assetValueLineChartFactory;
	
	@Autowired
	public ChartNodeFactory(
			IncomeOutgoingPieChartFactory incomeOutgoingPieChartFactory,
			TransactionsBarChartFactory transactionsBarChartFactory,
			AssetValueLineChartFactory assetValueLineChartFactory) {
		
		this.incomeOutgoingPieChartFactory = incomeOutgoingPieChartFactory;
		this.transactionsBarChartFactory = transactionsBarChartFactory;
		this.assetValueLineChartFactory = assetValueLineChartFactory;
	}
	
	public <T> Node createChart(T result, AnalysisType analysisType) {
		switch (analysisType) {
			case ABSOLUTE_ASSET_VALUE:
			case AGGREGATED_ASSET_VALUE:
			case ABSOLUTE_AND_AGGREGATED_ASSET_VALUE:
				return assetValueLineChartFactory.createAssetValueLineChart((List<DtoChartSeries<Number, Number>>) result, analysisType);
			case TRANSACTIONS:
				return transactionsBarChartFactory.createTransactionsBarChart((List<DtoChartSeries<String, Number>>) result, analysisType);
			case INCOME:
			case OUTGOING:
				return incomeOutgoingPieChartFactory.createIncomeOutgoingPieChart((List<DtoChartSeries<Object, Number>>) result, analysisType);
			default:
				String message = String.format("Could not show chart for unknown analysis type '%s'.", analysisType);
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
