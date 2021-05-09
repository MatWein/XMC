package io.github.matwein.xmc.common.stubs.depot.transactions;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.common.stubs.IPagingField;

import static io.github.matwein.xmc.be.entities.depot.QDepotTransaction.depotTransaction;
import static io.github.matwein.xmc.be.entities.depot.QStock.stock;

public enum DepotTransactionOverviewFields implements IPagingField {
	VALUTA_DATE(depotTransaction.valutaDate),
	ISIN(depotTransaction.isin),
	WKN(stock.wkn),
	NAME(stock.name),
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
