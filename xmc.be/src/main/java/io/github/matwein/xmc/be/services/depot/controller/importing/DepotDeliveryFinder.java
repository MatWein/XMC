package io.github.matwein.xmc.be.services.depot.controller.importing;

import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DepotDeliveryFinder {
	public Optional<DepotDelivery> findExistingDeliveries(List<DepotDelivery> existingDeliveries, LocalDateTime deliveryDate) {
		List<DepotDelivery> matches = existingDeliveries.stream()
				.filter(delivery -> delivery.getDeliveryDate().equals(deliveryDate))
				.collect(Collectors.toList());
		
		if (matches.size() == 1) {
			return Optional.of(matches.get(0));
		}
		return Optional.empty();
	}
}
