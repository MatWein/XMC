package org.xmc.common.stubs.bank;

import com.querydsl.core.types.Expression;
import org.xmc.common.stubs.IPagingField;

import static org.xmc.be.entities.QBank.bank;

public enum BankOverviewFields implements IPagingField {
    BANK_NAME(bank.name),
    BANK_BIC(bank.bic),
    BANK_BLZ(bank.blz),
    CREATION_DATE(bank.creationDate);

    private final Expression<?> expression;

    BankOverviewFields(Expression<?> expression) {
        this.expression = expression;
    }

    @Override
    public Expression<?> getExpression() {
        return expression;
    }
}
