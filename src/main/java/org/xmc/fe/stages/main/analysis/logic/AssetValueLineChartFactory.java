package org.xmc.fe.stages.main.analysis.logic;

import javafx.scene.chart.NumberAxis;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.AnalysisType;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.charts.ExtendedLineChart;
import org.xmc.fe.ui.charts.LocalDateTimeAxis;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;

import java.util.List;

@Component
public class AssetValueLineChartFactory {
	public ExtendedLineChart<Number, Number> createAssetValueLineChart(
			List<DtoChartSeries<Number, Number>> series,
			AnalysisType analysisType) {
		
		NumberAxis xAxis = LocalDateTimeAxis.createAxis();
		xAxis.setLabel(MessageAdapter.getByKey(MessageKey.ANALYSIS_AXIS_DATE));
		
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel(MessageAdapter.getByKey(MessageKey.ANALYSIS_AXIS_VALUE_IN_EUR));
		yAxis.setTickLabelFormatter(GenericItemToStringConverter.getInstance(MessageAdapter::formatNumber));
		
		ExtendedLineChart<Number, Number> lineChart = new ExtendedLineChart<>(xAxis, yAxis);
		lineChart.setTitle(MessageAdapter.getByKey(MessageKey.ANALYSIS_TYPE, analysisType));
		lineChart.setShowHoverLabel(true);
		lineChart.applyData(series);
		
		return lineChart;
	}
}
