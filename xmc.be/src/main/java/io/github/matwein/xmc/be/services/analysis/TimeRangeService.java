package io.github.matwein.xmc.be.services.analysis;

import com.google.common.collect.Multimap;
import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.services.analysis.calculation.TimeRangeCalculator;
import io.github.matwein.xmc.common.services.analysis.ITimeRangeService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.TimeRange;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class TimeRangeService implements ITimeRangeService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeRangeService.class);
	
	private final TimeRangeCalculator timeRangeCalculator;
	
	@Autowired
	public TimeRangeService(TimeRangeCalculator timeRangeCalculator) {
		this.timeRangeCalculator = timeRangeCalculator;
	}
	
	@Override
	public Pair<LocalDate, LocalDate> calculateStartAndEndDate(
			IAsyncMonitor monitor,
			TimeRange timeRange,
			Multimap<AssetType, Long> assetIds) {
		
		LOGGER.info("Calculating start/end date for time range '{}' for: {}.", timeRange, assetIds);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_CALCULATE_STARTEND_DATE));
		
		return timeRangeCalculator.calculateStartAndEndDate(timeRange, assetIds);
	}
}
