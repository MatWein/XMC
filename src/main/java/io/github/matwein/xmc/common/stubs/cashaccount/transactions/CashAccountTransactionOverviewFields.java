package io.github.matwein.xmc.common.stubs.cashaccount.transactions;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.common.stubs.IPagingField;

import static io.github.matwein.xmc.be.entities.cashaccount.QCashAccountTransaction.cashAccountTransaction;
import static io.github.matwein.xmc.be.entities.cashaccount.QCategory.category;

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
    MANDATE(cashAccountTransaction.mandate),
	CREATION_DATE(cashAccountTransaction.creationDate)
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
