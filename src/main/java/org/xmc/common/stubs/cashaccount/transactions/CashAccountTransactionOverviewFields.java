package org.xmc.common.stubs.cashaccount.transactions;

import com.querydsl.core.types.Expression;
import org.xmc.common.stubs.IPagingField;

import static org.xmc.be.entities.cashaccount.QCashAccountTransaction.cashAccountTransaction;
import static org.xmc.be.entities.cashaccount.QCategory.category;

public enum CashAccountTransactionOverviewFields implements IPagingField {
    CATEGORY_NAME(category.name),
    VALUTA_DATE(cashAccountTransaction.valutaDate),
    USAGE(cashAccountTransaction.usage),
    VALUE(cashAccountTransaction.value),
    DESCRIPTION(cashAccountTransaction.description),
    REFERENCE_BANK(cashAccountTransaction.referenceBank),
    REFERENCE_IBAN(cashAccountTransaction.referenceIban),
    REFERENCE(cashAccountTransaction.reference),
    CREDITOR_IDENTIFIER(cashAccountTransaction.creditorIdentifier),
    MANDATE(cashAccountTransaction.mandate);

    private final Expression<?> expression;

    CashAccountTransactionOverviewFields(Expression<?> expression) {
        this.expression = expression;
    }

    @Override
    public Expression<?> getExpression() {
        return expression;
    }
}
