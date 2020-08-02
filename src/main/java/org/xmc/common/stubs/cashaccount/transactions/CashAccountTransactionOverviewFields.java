package org.xmc.common.stubs.cashaccount.transactions;

import com.querydsl.core.types.Expression;
import org.xmc.common.stubs.IPagingField;

public enum CashAccountTransactionOverviewFields implements IPagingField {
    ;

    private final Expression<?> expression;

    CashAccountTransactionOverviewFields(Expression<?> expression) {
        this.expression = expression;
    }

    @Override
    public Expression<?> getExpression() {
        return expression;
    }
}
