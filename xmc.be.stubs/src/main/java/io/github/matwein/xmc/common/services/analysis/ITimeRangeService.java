package io.github.matwein.xmc.common.services.analysis;

import com.google.common.collect.Multimap;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.TimeRange;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;

public interface ITimeRangeService {
	Pair<LocalDate, LocalDate> calculateStartAndEndDate(
			IAsyncMonitor monitor,
			TimeRange timeRange,
			Multimap<AssetType, Long> assetIds);
}
