package org.xmc.be.services.analysis.calculation;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;

import java.time.LocalDate;
import java.util.List;

@Component
public class AbsoluteAssetValueLineChartAggregator {
	public DtoChartSeries<LocalDate, Number> aggregate(List<DtoChartSeries<LocalDate, Number>> assetLines) {
		return new DtoChartSeries<>();
	}
}
