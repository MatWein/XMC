package io.github.matwein.xmc.be.services.analysis.mapper;

import org.springframework.stereotype.Component;
import io.github.matwein.xmc.be.entities.analysis.AnalysisFavourite;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.common.stubs.analysis.DtoAnalysisFavourite;

import java.util.List;

@Component
public class DtoAnalysisFavouriteToAnalysisFavouriteMapper {
	public AnalysisFavourite map(DtoAnalysisFavourite analysisToSave, List<CashAccount> cashAccounts, List<Depot> depots) {
		var analysisFavourite = new AnalysisFavourite();
		
		analysisFavourite.setName(analysisToSave.getName());
		analysisFavourite.setStartDate(analysisToSave.getStartDate());
		analysisFavourite.setEndDate(analysisToSave.getEndDate());
		analysisFavourite.setType(analysisToSave.getAnalysisType());
		analysisFavourite.setTimeRange(analysisToSave.getTimeRange());
		
		analysisFavourite.getCashAccounts().addAll(cashAccounts);
		analysisFavourite.getDepots().addAll(depots);
		
		return analysisFavourite;
	}
}
