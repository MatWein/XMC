package org.xmc.common.stubs.cashaccount;

import com.querydsl.core.types.Expression;
import org.xmc.common.stubs.IPagingField;

import static org.xmc.be.entities.cashaccount.QCashAccount.cashAccount;

public enum CashAccountOverviewFields implements IPagingField {
    BANK_NAME(cashAccount.bank().name),
    BANK_BIC(cashAccount.bank().bic),
    BANK_BLZ(cashAccount.bank().blz),

    NAME(cashAccount.name),
    IBAN(cashAccount.iban),
    NUMBER(cashAccount.number),
    CREATION_DATE(cashAccount.creationDate),
    CURRENCY(cashAccount.currency),
    LAST_SALDO(cashAccount.lastSaldo),
    LAST_SALDO_DATE(cashAccount.lastSaldoDate);

    private final Expression<?> expression;

    CashAccountOverviewFields(Expression<?> expression) {
        this.expression = expression;
    }

    @Override
    public Expression<?> getExpression() {
        return expression;
    }
}
