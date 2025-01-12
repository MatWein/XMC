package io.github.matwein.xmc.be.common.pagination.mapper;

import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.common.stubs.ccf.CurrencyConversionFactorOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class CurrencyConversionFactorOverviewFieldsMapper implements IPagingFieldMapper<CurrencyConversionFactorOverviewFields> {
	@Override
	public String map(CurrencyConversionFactorOverviewFields pagingField) {
		return switch (pagingField) {
			case INPUT_DATE -> "ccf.inputDate";
			case CURRENCY -> "ccf.currency";
			case FACTOR_TO_EUR -> "ccf.factorToEur";
		};
	}
}
