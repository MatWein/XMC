package org.xmc.be.services.analysis.mapper;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.PersistentObject;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.depot.Depot;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAssetSelection;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DtoAssetSelectionMapper {
	public List<DtoAssetSelection> mapCashAccounts(List<CashAccount> cashAccountEntities) {
		return mapAll(cashAccountEntities, CashAccount::getName, AssetType.CASHACCOUNT);
	}
	
	public List<DtoAssetSelection> mapDepots(List<Depot> depotEntities) {
		return mapAll(depotEntities, Depot::getName, AssetType.DEPOT);
	}
	
	private <T extends PersistentObject> List<DtoAssetSelection> mapAll(
			List<T> entities,
			Function<T, String> nameExtractor,
			AssetType assetType) {
		
		return entities.stream()
				.map(entity -> map(nameExtractor, assetType, entity))
				.collect(Collectors.toList());
	}
	
	private <T extends PersistentObject> DtoAssetSelection map(Function<T, String> nameExtractor, AssetType assetType, T entity) {
		var dtoAssetSelection = new DtoAssetSelection();
		
		dtoAssetSelection.setId(entity.getId());
		dtoAssetSelection.setName(nameExtractor.apply(entity));
		dtoAssetSelection.setAssetType(assetType);
		
		return dtoAssetSelection;
	}
}
