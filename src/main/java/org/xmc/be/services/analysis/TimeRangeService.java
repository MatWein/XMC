package org.xmc.be.services.analysis;

import com.google.common.collect.Multimap;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.services.analysis.calculation.TimeRangeCalculator;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.TimeRange;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDate;

@Service
@Transactional
public class TimeRangeService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeRangeService.class);
	
	private final TimeRangeCalculator timeRangeCalculator;
	
	@Autowired
	public TimeRangeService(TimeRangeCalculator timeRangeCalculator) {
		this.timeRangeCalculator = timeRangeCalculator;
	}
	
	public Pair<LocalDate, LocalDate> calculateStartAndEndDate(
			AsyncMonitor monitor,
			TimeRange timeRange,
			Multimap<AssetType, Long> assetIds) {
		
		LOGGER.info("Calculating start/end date for time range '{}' for: {}.", timeRange, assetIds);
		monitor.setStatusText(MessageKey.ASYNC_TASK_CALCULATE_STARTEND_DATE);
		
		return timeRangeCalculator.calculateStartAndEndDate(timeRange, assetIds);
	}
}
