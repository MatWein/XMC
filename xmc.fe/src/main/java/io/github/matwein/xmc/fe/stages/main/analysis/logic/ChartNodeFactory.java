package io.github.matwein.xmc.fe.stages.main.analysis.logic;

import io.github.matwein.xmc.common.stubs.analysis.AnalysisType;
import io.github.matwein.xmc.common.stubs.analysis.DtoMostRecentTransaction;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChartNodeFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChartNodeFactory.class);
	
	private final IncomeOutgoingPieChartFactory incomeOutgoingPieChartFactory;
	private final TransactionsBarChartFactory transactionsBarChartFactory;
	private final AssetValueLineChartFactory assetValueLineChartFactory;
	private final MostRecentTransactionNodeFactory mostRecentTransactionNodeFactory;
	
	@Autowired
	public ChartNodeFactory(
			IncomeOutgoingPieChartFactory incomeOutgoingPieChartFactory,
			TransactionsBarChartFactory transactionsBarChartFactory,
			AssetValueLineChartFactory assetValueLineChartFactory,
			MostRecentTransactionNodeFactory mostRecentTransactionNodeFactory) {
		
		this.incomeOutgoingPieChartFactory = incomeOutgoingPieChartFactory;
		this.transactionsBarChartFactory = transactionsBarChartFactory;
		this.assetValueLineChartFactory = assetValueLineChartFactory;
		this.mostRecentTransactionNodeFactory = mostRecentTransactionNodeFactory;
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
			case MOST_RECENT_TRANSACTIONS:
				return mostRecentTransactionNodeFactory.createNode((List<DtoMostRecentTransaction>)result);
			default:
				String message = String.format("Could not show chart for unknown analysis type '%s'.", analysisType);
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
