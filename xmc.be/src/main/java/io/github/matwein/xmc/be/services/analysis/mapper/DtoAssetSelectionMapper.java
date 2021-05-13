package io.github.matwein.xmc.be.services.analysis.mapper;

import io.github.matwein.xmc.be.entities.PersistentObject;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoAssetSelection;
import org.springframework.stereotype.Component;

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
