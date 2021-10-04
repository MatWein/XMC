package io.github.matwein.xmc.fe.stages.main.analysis.logic;

import io.github.matwein.xmc.common.stubs.analysis.AnalysisType;
import io.github.matwein.xmc.common.stubs.analysis.DtoMostRecentTransaction;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChartNodeFactory {
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
		return switch (analysisType) {
			case ABSOLUTE_ASSET_VALUE, AGGREGATED_ASSET_VALUE, ABSOLUTE_AND_AGGREGATED_ASSET_VALUE -> assetValueLineChartFactory.createAssetValueLineChart((List<DtoChartSeries<Number, Number>>) result, analysisType);
			case TRANSACTIONS -> transactionsBarChartFactory.createTransactionsBarChart((List<DtoChartSeries<String, Number>>) result, analysisType);
			case INCOME, OUTGOING -> incomeOutgoingPieChartFactory.createIncomeOutgoingPieChart((List<DtoChartSeries<Object, Number>>) result, analysisType);
			case MOST_RECENT_TRANSACTIONS -> mostRecentTransactionNodeFactory.createNode((List<DtoMostRecentTransaction>) result);
		};
	}
}
