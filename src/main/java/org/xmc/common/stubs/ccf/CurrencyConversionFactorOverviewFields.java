package org.xmc.common.stubs.ccf;

import com.querydsl.core.types.Expression;
import org.xmc.common.stubs.IPagingField;

import static org.xmc.be.entities.depot.QCurrencyConversionFactor.currencyConversionFactor;

public enum CurrencyConversionFactorOverviewFields implements IPagingField {
	INPUT_DATE(currencyConversionFactor.inputDate),
	CURRENCY(currencyConversionFactor.currency),
	FACTOR_TO_EUR(currencyConversionFactor.factorToEur);
	
	private final Expression<?> expression;
	
	CurrencyConversionFactorOverviewFields(Expression<?> expression) {
		this.expression = expression;
	}
	
	@Override
	public Expression<?> getExpression() {
		return expression;
	}
}
