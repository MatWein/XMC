package org.xmc.be.services.analysis.mapper;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.analysis.AnalysisFavourite;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.depot.Depot;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAnalysisFavourite;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AnalysisFavouriteToDtoAnalysisFavouriteMapper {
	public List<DtoAnalysisFavourite> mapAll(List<AnalysisFavourite> analysisFavourites) {
		return analysisFavourites.stream()
				.map(this::map)
				.sorted(Comparator.comparing(DtoAnalysisFavourite::getName))
				.collect(Collectors.toList());
	}
	
	private DtoAnalysisFavourite map(AnalysisFavourite analysisFavourite) {
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
