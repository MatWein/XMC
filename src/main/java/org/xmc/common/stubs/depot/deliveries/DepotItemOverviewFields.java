package org.xmc.common.stubs.depot.deliveries;

import com.querydsl.core.types.Expression;
import org.xmc.common.stubs.IPagingField;

import static org.xmc.be.entities.depot.QDepotItem.depotItem;
import static org.xmc.be.entities.depot.QStock.stock;

public enum DepotItemOverviewFields implements IPagingField {
	ISIN(depotItem.isin),
	WKN(stock.wkn),
	NAME(stock.name),
	AMOUNT(depotItem.amount),
	COURSE(depotItem.course),
	VALUE(depotItem.value),
	CURRENCY(depotItem.currency),
	CREATION_DATE(depotItem.creationDate)
	;
	
	private final Expression<?> expression;
	
	DepotItemOverviewFields(Expression<?> expression) {
		this.expression = expression;
	}
	
	@Override
	public Expression<?> getExpression() {
		return expression;
	}
}
