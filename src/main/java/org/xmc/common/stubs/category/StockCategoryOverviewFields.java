package org.xmc.common.stubs.category;

import com.querydsl.core.types.Expression;
import org.xmc.common.stubs.IPagingField;

import static org.xmc.be.entities.depot.QStockCategory.stockCategory;

public enum StockCategoryOverviewFields implements IPagingField {
    NAME(stockCategory.name),
    CREATION_DATE(stockCategory.creationDate);

    private final Expression<?> expression;

    StockCategoryOverviewFields(Expression<?> expression) {
        this.expression = expression;
    }

    @Override
    public Expression<?> getExpression() {
        return expression;
    }
}