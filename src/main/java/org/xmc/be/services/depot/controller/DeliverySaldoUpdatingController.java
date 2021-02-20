package org.xmc.be.services.depot.controller;

import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.CurrencyConversionFactor;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.be.entities.depot.DepotItem;
import org.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import org.xmc.be.services.ccf.controller.CurrencyConversionFactorLoadingController;
import org.xmc.be.services.depot.calculation.DepotItemEuroValueCalculator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DeliverySaldoUpdatingController {
	private final DepotDeliveryJpaRepository depotDeliveryJpaRepository;
	private final CurrencyConversionFactorLoadingController currencyConversionFactorLoadingController;
	private final DepotItemEuroValueCalculator depotItemEuroValueCalculator;
	
	@Autowired
	public DeliverySaldoUpdatingController(
			DepotDeliveryJpaRepository depotDeliveryJpaRepository,
			CurrencyConversionFactorLoadingController currencyConversionFactorLoadingController,
			DepotItemEuroValueCalculator depotItemEuroValueCalculator) {
		
		this.depotDeliveryJpaRepository = depotDeliveryJpaRepository;
		this.currencyConversionFactorLoadingController = currencyConversionFactorLoadingController;
		this.depotItemEuroValueCalculator = depotItemEuroValueCalculator;
	}
	
	public void updateDeliverySaldoForAllDeliveries(LocalDateTime startDate) {
		depotDeliveryJpaRepository.flush();
		
		List<DepotDelivery> deliveriesToUpdate = depotDeliveryJpaRepository.findByDeliveryDateGreaterThanEqualAndDeletionDateIsNull(startDate);
		
		Set<String> currencies = deliveriesToUpdate.stream()
				.flatMap(delivery -> delivery.getDepotItems().stream())
				.map(DepotItem::getCurrency)
				.collect(Collectors.toSet());
		Multimap<String, CurrencyConversionFactor> currencyConversionFactors = currencyConversionFactorLoadingController.load(currencies);
		
		for (DepotDelivery depotDelivery : deliveriesToUpdate) {
			updateDeliverySaldo(depotDelivery, currencyConversionFactors);
		}
	}
	
	public void updateDeliverySaldo(DepotDelivery depotDelivery) {
		depotDeliveryJpaRepository.flush();
		
		depotDelivery = depotDeliveryJpaRepository.getOne(depotDelivery.getId());
		
		Set<String> currencies = depotDelivery.getDepotItems().stream()
				.filter(depotItem -> depotItem.getDeletionDate() == null)
				.map(DepotItem::getCurrency)
				.collect(Collectors.toSet());
		Multimap<String, CurrencyConversionFactor> currencyConversionFactors = currencyConversionFactorLoadingController.load(currencies);
		
		updateDeliverySaldo(depotDelivery, currencyConversionFactors);
	}
	
	private void updateDeliverySaldo(DepotDelivery depotDelivery, Multimap<String, CurrencyConversionFactor> currencyConversionFactors) {
		BigDecimal sum = depotDelivery.getDepotItems().stream()
				.filter(depotItem -> depotItem.getDeletionDate() == null)
				.map(depotItem -> depotItemEuroValueCalculator.calculateEuroValue(depotItem, currencyConversionFactors))
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.valueOf(0.0));
		
		depotDelivery.setSaldo(sum);
		depotDeliveryJpaRepository.save(depotDelivery);
	}
}
