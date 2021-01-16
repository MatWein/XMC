package org.xmc.common.stubs.stocks;

import com.querydsl.core.types.Expression;
import org.xmc.common.stubs.IPagingField;

import static org.xmc.be.entities.depot.QStock.stock;

public enum StockOverviewFields implements IPagingField {
	ISIN(stock.isin),
	WKN(stock.wkn),
	NAME(stock.name),
	STOCK_CATEGORY(stock.stockCategory().name),
	CREATION_DATE(stock.creationDate);
	
	private final Expression<?> expression;
	
	StockOverviewFields(Expression<?> expression) {
		this.expression = expression;
	}
	
	@Override
	public Expression<?> getExpression() {
		return expression;
	}
}
