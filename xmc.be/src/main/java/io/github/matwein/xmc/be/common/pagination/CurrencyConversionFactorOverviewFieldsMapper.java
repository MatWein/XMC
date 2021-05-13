package io.github.matwein.xmc.be.common.pagination;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.entities.depot.QCurrencyConversionFactor;
import io.github.matwein.xmc.common.stubs.ccf.CurrencyConversionFactorOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CurrencyConversionFactorOverviewFieldsMapper implements IPagingFieldMapper<CurrencyConversionFactorOverviewFields> {
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyConversionFactorOverviewFieldsMapper.class);
	
	@Override
	public Expression<?> map(CurrencyConversionFactorOverviewFields pagingField) {
		switch (pagingField) {
			case INPUT_DATE:
				return QCurrencyConversionFactor.currencyConversionFactor.inputDate;
			case CURRENCY:
				return QCurrencyConversionFactor.currencyConversionFactor.currency;
			case FACTOR_TO_EUR:
				return QCurrencyConversionFactor.currencyConversionFactor.factorToEur;
			default:
				String message = String.format("Could not map enum value '%s' of type '%s' to expression.", pagingField, pagingField.getClass().getSimpleName());
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
