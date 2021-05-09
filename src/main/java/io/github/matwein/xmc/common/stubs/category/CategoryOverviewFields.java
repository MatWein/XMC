package io.github.matwein.xmc.common.stubs.category;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.common.stubs.IPagingField;

import static io.github.matwein.xmc.be.entities.cashaccount.QCategory.category;

public enum CategoryOverviewFields implements IPagingField {
    NAME(category.name),
    CREATION_DATE(category.creationDate);

    private final Expression<?> expression;

    CategoryOverviewFields(Expression<?> expression) {
        this.expression = expression;
    }

    @Override
    public Expression<?> getExpression() {
        return expression;
    }
}
