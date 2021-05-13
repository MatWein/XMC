package io.github.matwein.xmc.be.services.analysis.controller;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotJpaRepository;
import io.github.matwein.xmc.be.services.analysis.mapper.DtoAssetSelectionMapper;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoAssetSelection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssetSelectionLoadingController {
	private final CashAccountJpaRepository cashAccountJpaRepository;
	private final DepotJpaRepository depotJpaRepository;
	private final DtoAssetSelectionMapper dtoAssetSelectionMapper;
	
	@Autowired
	public AssetSelectionLoadingController(
			CashAccountJpaRepository cashAccountJpaRepository,
			DepotJpaRepository depotJpaRepository,
			DtoAssetSelectionMapper dtoAssetSelectionMapper) {
		
		this.cashAccountJpaRepository = cashAccountJpaRepository;
		this.depotJpaRepository = depotJpaRepository;
		this.dtoAssetSelectionMapper = dtoAssetSelectionMapper;
	}
	
	public DtoAssetSelection loadSelectableAssets() {
		var root = new DtoAssetSelection();
		root.setName(MessageAdapter.getByKey(MessageKey.ASSET_TYPE_ALL));
		
		var cashAccounts = new DtoAssetSelection(AssetType.CASHACCOUNT, MessageAdapter.getByKey(MessageKey.ASSET_TYPE, AssetType.CASHACCOUNT));
		root.getChildren().add(cashAccounts);
		
		List<CashAccount> cashAccountEntities = cashAccountJpaRepository.findByDeletionDateIsNull();
		cashAccounts.getChildren().addAll(dtoAssetSelectionMapper.mapCashAccounts(cashAccountEntities));
		
		var depots = new DtoAssetSelection(AssetType.DEPOT, MessageAdapter.getByKey(MessageKey.ASSET_TYPE, AssetType.DEPOT));
		root.getChildren().add(depots);
		
		List<Depot> depotEntities = depotJpaRepository.findByDeletionDateIsNull();
		depots.getChildren().addAll(dtoAssetSelectionMapper.mapDepots(depotEntities));
		
		return root;
	}
}
