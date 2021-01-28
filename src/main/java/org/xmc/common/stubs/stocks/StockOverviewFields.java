package org.xmc.common.stubs.stocks;

import com.querydsl.core.types.Expression;
import org.xmc.common.stubs.IPagingField;

import static org.xmc.be.entities.depot.QStock.stock;
import static org.xmc.be.entities.depot.QStockCategory.stockCategory;

public enum StockOverviewFields implements IPagingField {
	ISIN(stock.isin),
	WKN(stock.wkn),
	NAME(stock.name),
	STOCK_CATEGORY(stockCategory.name),
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
