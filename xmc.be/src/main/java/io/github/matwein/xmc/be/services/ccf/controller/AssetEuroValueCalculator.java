package io.github.matwein.xmc.be.services.ccf.controller;

import com.google.common.collect.Multimap;
import io.github.matwein.xmc.be.entities.depot.CurrencyConversionFactor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Currency;
import java.util.Optional;

@Component
public class AssetEuroValueCalculator {
	private static final BigDecimal DEFAULT_FACTOR = BigDecimal.valueOf(1.0);
	
	public BigDecimal calculateEuroValue(
			BigDecimal valueToConvert,
			LocalDateTime date,
			Currency sourceCurrency,
			Multimap<String, CurrencyConversionFactor> currencyConversionFactors) {
		
		return calculateEuroValue(valueToConvert, date, sourceCurrency.getCurrencyCode(), currencyConversionFactors);
	}
	
	public BigDecimal calculateEuroValue(
			BigDecimal valueToConvert,
			LocalDateTime date,
			String sourceCurrency,
			Multimap<String, CurrencyConversionFactor> currencyConversionFactors) {
		
		BigDecimal conversionFactor = findConversionFactor(sourceCurrency, date, currencyConversionFactors);
		return valueToConvert.multiply(conversionFactor);
	}
	
	private BigDecimal findConversionFactor(
			String sourceCurrency,
			LocalDateTime date,
			Multimap<String, CurrencyConversionFactor> currencyConversionFactors) {
		
		Collection<CurrencyConversionFactor> conversionFactors = currencyConversionFactors.get(sourceCurrency);
		if (CollectionUtils.isEmpty(conversionFactors)) {
			return DEFAULT_FACTOR;
		}
		
		Optional<CurrencyConversionFactor> mostRecentConversionFactor = conversionFactors.stream()
				.filter(ccf -> ccf.getInputDate().isBefore(date))
				.max(Comparator.comparing(CurrencyConversionFactor::getInputDate));
		
		if (mostRecentConversionFactor.isEmpty()) {
			return DEFAULT_FACTOR;
		}
		
		return mostRecentConversionFactor.get().getFactorToEur();
	}
}
