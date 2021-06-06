package io.github.matwein.xmc.be.services.analysis.mapper;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.matwein.xmc.be.entities.analysis.AnalysisFavourite;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.services.analysis.calculation.TimeRangeCalculator;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoAnalysisFavourite;
import io.github.matwein.xmc.common.stubs.analysis.TimeRange;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AnalysisFavouriteToDtoAnalysisFavouriteMapper {
	private final TimeRangeCalculator timeRangeCalculator;
	
	@Autowired
	public AnalysisFavouriteToDtoAnalysisFavouriteMapper(TimeRangeCalculator timeRangeCalculator) {
		this.timeRangeCalculator = timeRangeCalculator;
	}
	
	public List<DtoAnalysisFavourite> mapAll(List<AnalysisFavourite> analysisFavourites) {
		return analysisFavourites.stream()
				.map(this::map)
				.peek(this::recalculateStartAndEndDate)
				.sorted(Comparator.comparing(DtoAnalysisFavourite::getName))
				.collect(Collectors.toList());
	}
	
	private void recalculateStartAndEndDate(DtoAnalysisFavourite favourite) {
		if (favourite.getTimeRange() == TimeRange.USER_DEFINED) {
			return;
		}
		
		Pair<LocalDate, LocalDate> timeRangeDates = timeRangeCalculator.calculateStartAndEndDate(favourite.getTimeRange(), favourite.getAssetIds());
		favourite.setStartDate(timeRangeDates.getLeft());
		favourite.setEndDate(timeRangeDates.getRight());
	}
	
	public DtoAnalysisFavourite map(AnalysisFavourite analysisFavourite) {
		var dtoAnalysisFavourite = new DtoAnalysisFavourite();
		
		dtoAnalysisFavourite.setName(analysisFavourite.getName());
		dtoAnalysisFavourite.setAnalysisType(analysisFavourite.getType());
		dtoAnalysisFavourite.setTimeRange(analysisFavourite.getTimeRange());
		dtoAnalysisFavourite.setStartDate(analysisFavourite.getStartDate());
		dtoAnalysisFavourite.setEndDate(analysisFavourite.getEndDate());
		dtoAnalysisFavourite.setAssetIds(mapAssetIds(analysisFavourite.getCashAccounts(), analysisFavourite.getDepots()));
		
		return dtoAnalysisFavourite;
	}
	
	private Multimap<AssetType, Long> mapAssetIds(Set<CashAccount> cashAccounts, Set<Depot> depots) {
		Multimap<AssetType, Long> assetIds = ArrayListMultimap.create();
		
		for (CashAccount cashAccount : cashAccounts) {
			assetIds.put(AssetType.CASHACCOUNT, cashAccount.getId());
		}
		
		for (Depot depot : depots) {
			assetIds.put(AssetType.DEPOT, depot.getId());
		}
		
		return assetIds;
	}
}
