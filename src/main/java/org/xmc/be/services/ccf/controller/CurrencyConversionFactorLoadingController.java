package org.xmc.be.services.ccf.controller;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.CurrencyConversionFactor;
import org.xmc.be.repositories.ccf.CurrencyConversionFactorJpaRepository;

import java.util.List;
import java.util.Set;

@Component
public class CurrencyConversionFactorLoadingController {
	private final CurrencyConversionFactorJpaRepository currencyConversionFactorJpaRepository;
	
	@Autowired
	public CurrencyConversionFactorLoadingController(CurrencyConversionFactorJpaRepository currencyConversionFactorJpaRepository) {
		this.currencyConversionFactorJpaRepository = currencyConversionFactorJpaRepository;
	}
	
	public Multimap<String, CurrencyConversionFactor> load(Set<String> currencies) {
		List<CurrencyConversionFactor> conversionFactors = currencyConversionFactorJpaRepository.findByCurrencyIn(currencies);
		return Multimaps.index(conversionFactors, CurrencyConversionFactor::getCurrency);
	}
}
