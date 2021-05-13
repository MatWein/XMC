package io.github.matwein.xmc.be.services.ccf.controller;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import io.github.matwein.xmc.be.entities.depot.CurrencyConversionFactor;
import io.github.matwein.xmc.be.repositories.ccf.CurrencyConversionFactorJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.List;
import java.util.Set;

@Component
public class CurrencyConversionFactorLoadingController {
	private final CurrencyConversionFactorJpaRepository currencyConversionFactorJpaRepository;
	
	@Autowired
	public CurrencyConversionFactorLoadingController(CurrencyConversionFactorJpaRepository currencyConversionFactorJpaRepository) {
		this.currencyConversionFactorJpaRepository = currencyConversionFactorJpaRepository;
	}
	
	public Multimap<String, CurrencyConversionFactor> load(Currency currency) {
		return load(currency.getCurrencyCode());
	}
	
	public Multimap<String, CurrencyConversionFactor> load(String currency) {
		return load(Sets.newHashSet(currency));
	}
	
	public Multimap<String, CurrencyConversionFactor> load(Set<String> currencies) {
		List<CurrencyConversionFactor> conversionFactors = currencyConversionFactorJpaRepository.findByCurrencyIn(currencies);
		return Multimaps.index(conversionFactors, CurrencyConversionFactor::getCurrency);
	}
}
