package io.github.matwein.xmc.be.common.pagination.mapper;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.be.entities.depot.QCurrencyConversionFactor;
import io.github.matwein.xmc.common.stubs.ccf.CurrencyConversionFactorOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class CurrencyConversionFactorOverviewFieldsMapper implements IPagingFieldMapper<CurrencyConversionFactorOverviewFields> {
	@Override
	public Expression<?> map(CurrencyConversionFactorOverviewFields pagingField) {
		return switch (pagingField) {
			case INPUT_DATE -> QCurrencyConversionFactor.currencyConversionFactor.inputDate;
			case CURRENCY -> QCurrencyConversionFactor.currencyConversionFactor.currency;
			case FACTOR_TO_EUR -> QCurrencyConversionFactor.currencyConversionFactor.factorToEur;
		};
	}
}
