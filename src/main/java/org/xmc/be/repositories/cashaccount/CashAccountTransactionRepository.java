package org.xmc.be.repositories.cashaccount;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmc.be.common.QueryUtil;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview;

import static org.xmc.be.entities.QBinaryData.binaryData;
import static org.xmc.be.entities.QCategory.category;
import static org.xmc.be.entities.cashaccount.QCashAccountTransaction.cashAccountTransaction;

@Repository
public class CashAccountTransactionRepository {
    private final QueryUtil queryUtil;

    @Autowired
    public CashAccountTransactionRepository(QueryUtil queryUtil) {
        this.queryUtil = queryUtil;
    }

    public QueryResults<DtoCashAccountTransactionOverview> loadOverview(PagingParams<CashAccountTransactionOverviewFields> pagingParams) {
        String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
        BooleanExpression predicate = cashAccountTransaction.usage.likeIgnoreCase(filter)
                .or(cashAccountTransaction.description.likeIgnoreCase(filter))
                .or(category.name.likeIgnoreCase(filter));

        return queryUtil.createPagedQuery(pagingParams, CashAccountTransactionOverviewFields.VALUTA_DATE, Order.DESC)
                .select(Projections.constructor(DtoCashAccountTransactionOverview.class,
                        cashAccountTransaction.id, category.id, category.name, binaryData.rawData,
                        cashAccountTransaction.usage, cashAccountTransaction.description,
                        cashAccountTransaction.valutaDate, cashAccountTransaction.value,
                        cashAccountTransaction.reference, cashAccountTransaction.referenceIban,
                        cashAccountTransaction.referenceBank, cashAccountTransaction.creditorIdentifier,
                        cashAccountTransaction.mandate, cashAccountTransaction.creationDate,
                        cashAccountTransaction.saldoBefore, cashAccountTransaction.saldoAfter))
                .from(cashAccountTransaction)
                .leftJoin(cashAccountTransaction.category(), category)
                .leftJoin(category.icon(), binaryData)
                .where(ExpressionUtils.allOf(predicate, cashAccountTransaction.deletionDate.isNull()))
                .orderBy(new OrderSpecifier<>(Order.DESC, cashAccountTransaction.id))
                .fetchResults();
    }
}
