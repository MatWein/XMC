package org.xmc.be.services.ccf.mapper;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.CurrencyConversionFactor;
import org.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;

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
