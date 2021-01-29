package org.xmc.common.stubs.depot.transactions;

import com.querydsl.core.types.Expression;
import org.xmc.common.stubs.IPagingField;

import static org.xmc.be.entities.depot.QDepotTransaction.depotTransaction;

public enum DepotTransactionOverviewFields implements IPagingField {
	VALUTA_DATE(depotTransaction.valutaDate),
	ISIN(depotTransaction.isin),
	AMOUNT(depotTransaction.amount),
	COURSE(depotTransaction.course),
	VALUE(depotTransaction.value),
	DESCRIPTION(depotTransaction.description),
	CURRENCY(depotTransaction.currency),
	CREATION_DATE(depotTransaction.creationDate)
	;
	
	private final Expression<?> expression;
	
	DepotTransactionOverviewFields(Expression<?> expression) {
		this.expression = expression;
	}
	
	@Override
	public Expression<?> getExpression() {
		return expression;
	}
}
