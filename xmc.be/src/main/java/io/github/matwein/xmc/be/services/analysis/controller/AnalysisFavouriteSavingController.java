package io.github.matwein.xmc.be.services.analysis.controller;

import io.github.matwein.xmc.be.entities.analysis.AnalysisFavourite;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.repositories.analysis.AnalysisFavouriteJpaRepository;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotJpaRepository;
import io.github.matwein.xmc.be.services.analysis.mapper.DtoAnalysisFavouriteToAnalysisFavouriteMapper;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoAnalysisFavourite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AnalysisFavouriteSavingController {
	private final AnalysisFavouriteJpaRepository analysisFavouriteJpaRepository;
	private final DtoAnalysisFavouriteToAnalysisFavouriteMapper dtoAnalysisFavouriteToAnalysisFavouriteMapper;
	private final CashAccountJpaRepository cashAccountJpaRepository;
	private final DepotJpaRepository depotJpaRepository;
	
	@Autowired
	public AnalysisFavouriteSavingController(
			AnalysisFavouriteJpaRepository analysisFavouriteJpaRepository,
			DtoAnalysisFavouriteToAnalysisFavouriteMapper dtoAnalysisFavouriteToAnalysisFavouriteMapper,
			CashAccountJpaRepository cashAccountJpaRepository,
			DepotJpaRepository depotJpaRepository) {
		
		this.analysisFavouriteJpaRepository = analysisFavouriteJpaRepository;
		this.dtoAnalysisFavouriteToAnalysisFavouriteMapper = dtoAnalysisFavouriteToAnalysisFavouriteMapper;
		this.cashAccountJpaRepository = cashAccountJpaRepository;
		this.depotJpaRepository = depotJpaRepository;
	}
	
	public void saveOrUpdate(DtoAnalysisFavourite analysisToSave) {
		deleteExistingAnalysis(analysisToSave);
		
		save(analysisToSave);
	}
	
	private void deleteExistingAnalysis(DtoAnalysisFavourite analysisToSave) {
		Optional<AnalysisFavourite> existingAnalysis = analysisFavouriteJpaRepository.findByName(analysisToSave.getName());
		
		if (existingAnalysis.isPresent()) {
			analysisFavouriteJpaRepository.delete(existingAnalysis.get());
			analysisFavouriteJpaRepository.flush();
		}
	}
	
	private void save(DtoAnalysisFavourite analysisToSave) {
		List<CashAccount> cashAccounts = cashAccountJpaRepository.findAllById(analysisToSave.getAssetIds().get(AssetType.CASHACCOUNT));
		List<Depot> depots = depotJpaRepository.findAllById(analysisToSave.getAssetIds().get(AssetType.DEPOT));
		
		AnalysisFavourite analysisFavourite = dtoAnalysisFavouriteToAnalysisFavouriteMapper.map(analysisToSave, cashAccounts, depots);
		
		analysisFavouriteJpaRepository.save(analysisFavourite);
	}
}
