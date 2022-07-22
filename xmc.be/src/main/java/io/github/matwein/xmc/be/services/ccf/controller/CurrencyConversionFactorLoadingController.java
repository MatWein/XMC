package io.github.matwein.xmc.be.services.ccf.controller;

import io.github.matwein.xmc.be.entities.depot.CurrencyConversionFactor;
import io.github.matwein.xmc.be.repositories.ccf.CurrencyConversionFactorJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CurrencyConversionFactorLoadingController {
	private final CurrencyConversionFactorJpaRepository currencyConversionFactorJpaRepository;
	
	@Autowired
	public CurrencyConversionFactorLoadingController(CurrencyConversionFactorJpaRepository currencyConversionFactorJpaRepository) {
		this.currencyConversionFactorJpaRepository = currencyConversionFactorJpaRepository;
	}
	
	public Map<String, List<CurrencyConversionFactor>> load(Currency currency) {
		return load(currency.getCurrencyCode());
	}
	
	public Map<String, List<CurrencyConversionFactor>> load(String currency) {
		return load(Set.of(currency));
	}
	
	public Map<String, List<CurrencyConversionFactor>> load(Set<String> currencies) {
		List<CurrencyConversionFactor> conversionFactors = currencyConversionFactorJpaRepository.findByCurrencyIn(currencies);
		
		return conversionFactors.stream()
				.collect(Collectors.groupingBy(CurrencyConversionFactor::getCurrency));
	}
}
