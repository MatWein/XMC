package org.xmc.be.services.depot.calculation;

import com.google.common.collect.Multimap;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.CurrencyConversionFactor;
import org.xmc.be.entities.depot.DepotItem;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Component
public class DepotItemEuroValueCalculator {
	private static final BigDecimal DEFAULT_FACTOR = BigDecimal.valueOf(1.0);
	
	public BigDecimal calculateEuroValue(DepotItem depotItem, Multimap<String, CurrencyConversionFactor> currencyConversionFactors) {
		BigDecimal conversionFactor = findConversionFactor(depotItem, currencyConversionFactors);
		return depotItem.getValue().multiply(conversionFactor);
	}
	
	private BigDecimal findConversionFactor(DepotItem depotItem, Multimap<String, CurrencyConversionFactor> currencyConversionFactors) {
		Collection<CurrencyConversionFactor> conversionFactors = currencyConversionFactors.get(depotItem.getCurrency());
		if (CollectionUtils.isEmpty(conversionFactors)) {
			return DEFAULT_FACTOR;
		}
		
		Optional<CurrencyConversionFactor> mostRecentConversionFactor = conversionFactors.stream()
				.filter(ccf -> ccf.getInputDate().isBefore(depotItem.getDelivery().getDeliveryDate()))
				.max(Comparator.comparing(CurrencyConversionFactor::getInputDate));
		
		if (mostRecentConversionFactor.isEmpty()) {
			return DEFAULT_FACTOR;
		}
		
		return mostRecentConversionFactor.get().getFactorToEur();
	}
}
