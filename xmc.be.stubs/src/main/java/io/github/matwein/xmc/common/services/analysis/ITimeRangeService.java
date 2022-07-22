package io.github.matwein.xmc.common.services.analysis;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.TimeRange;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ITimeRangeService {
	Pair<LocalDate, LocalDate> calculateStartAndEndDate(
			IAsyncMonitor monitor,
			TimeRange timeRange,
			Map<AssetType, List<Long>> assetIds);
}
