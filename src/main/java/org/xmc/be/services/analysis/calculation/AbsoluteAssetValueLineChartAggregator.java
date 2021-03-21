package org.xmc.be.services.analysis.calculation;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AbsoluteAssetValueLineChartAggregator {
	public DtoChartSeries<LocalDateTime, Number> aggregate(List<DtoChartSeries<LocalDateTime, Number>> assetLines) {
		return new DtoChartSeries<>();
	}
}
