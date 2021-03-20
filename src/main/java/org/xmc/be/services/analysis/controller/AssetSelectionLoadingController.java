package org.xmc.be.services.analysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import org.xmc.be.repositories.depot.DepotJpaRepository;
import org.xmc.be.services.analysis.mapper.DtoAssetSelectionMapper;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAssetSelection;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

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
