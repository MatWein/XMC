package io.github.matwein.xmc.fe.ui.charts.mapper;

import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartPoint;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;
import javafx.scene.chart.PieChart.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PieChartSeriesMapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(PieChartSeriesMapper.class);
	
	public List<Data> mapAll(List<DtoChartSeries<Object, Number>> series) {
		return series.stream()
				.map(this::mapSerie)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
	
	private Data mapSerie(DtoChartSeries<Object, Number> serie) {
		if (serie.getPoints().size() != 1) {
			LOGGER.error("Got invalid pie chart series data: {}. Pie chart series must have exactly one point. Serie will be filtered out.", serie.getPoints());
			return null;
		}
		
		DtoChartPoint<Object, Number> point = serie.getPoints().get(0);
		return new Data(serie.getName(), point.getY().doubleValue());
	}
}
