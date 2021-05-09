package io.github.matwein.xmc.be.services.depot.controller.importing;

import org.springframework.stereotype.Component;
import io.github.matwein.xmc.be.entities.depot.DepotItem;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemImportRow;

import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DepotItemFinder {
	public Optional<DepotItem> findExistingItem(List<DepotItem> existingDepotItems, DtoDepotItemImportRow row) {
		List<DepotItem> matches = existingDepotItems.stream()
				.filter(depotItem -> depotItem.getIsin().equalsIgnoreCase(row.getIsin()))
				.filter(depotItem -> depotItem.getAmount().setScale(2, RoundingMode.HALF_UP).equals(row.getAmount().setScale(2, RoundingMode.HALF_UP)))
				.filter(depotItem -> depotItem.getCurrency().equalsIgnoreCase(row.getCurrency().getCurrencyCode()))
				.collect(Collectors.toList());
		
		if (matches.size() == 1) {
			return Optional.of(matches.get(0));
		}
		return Optional.empty();
	}
}
