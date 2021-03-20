package org.xmc.be.services.analysis.calculation;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;

import java.time.LocalDate;
import java.util.List;

@Component
public class AbsoluteAssetValueLineChartCalculator {
	public List<DtoChartSeries<LocalDate, Number>> calculate(Multimap<AssetType, Long> assetIds, LocalDate startDate, LocalDate endDate) {
		return Lists.newArrayList();
	}
}
