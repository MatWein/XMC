package io.github.matwein.xmc.be.services.ccf.mapper;

import io.github.matwein.xmc.be.entities.depot.CurrencyConversionFactor;
import io.github.matwein.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;
import org.springframework.stereotype.Component;

@Component
public class DtoCurrencyConversionFactorToCurrencyConversionFactorMapper {
	public CurrencyConversionFactor map(DtoCurrencyConversionFactor dtoCurrencyConversionFactor) {
		var currencyConversionFactor = new CurrencyConversionFactor();
		
		update(currencyConversionFactor, dtoCurrencyConversionFactor);
		
		return currencyConversionFactor;
	}
	
	public void update(CurrencyConversionFactor conversionFactor, DtoCurrencyConversionFactor dtoCurrencyConversionFactor) {
		conversionFactor.setCurrency(dtoCurrencyConversionFactor.getCurrency().getCurrencyCode());
		conversionFactor.setFactorToEur(dtoCurrencyConversionFactor.getFactorToEur());
		conversionFactor.setInputDate(dtoCurrencyConversionFactor.getInputDate());
	}
}
