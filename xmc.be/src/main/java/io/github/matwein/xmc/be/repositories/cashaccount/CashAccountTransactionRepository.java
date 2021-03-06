package io.github.matwein.xmc.be.repositories.cashaccount;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.*;
import io.github.matwein.xmc.be.common.QueryUtil;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.common.stubs.Money;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview;
import io.github.matwein.xmc.common.stubs.category.DtoCategory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.querydsl.core.types.OrderSpecifier.NullHandling.NullsLast;
import static io.github.matwein.xmc.be.entities.QBinaryData.binaryData;
import static io.github.matwein.xmc.be.entities.cashaccount.QCashAccount.cashAccount;
import static io.github.matwein.xmc.be.entities.cashaccount.QCashAccountTransaction.cashAccountTransaction;
import static io.github.matwein.xmc.be.entities.cashaccount.QCategory.category;

@Repository
public class CashAccountTransactionRepository {
    private final QueryUtil queryUtil;

    @Autowired
    public CashAccountTransactionRepository(QueryUtil queryUtil) {
        this.queryUtil = queryUtil;
    }

    public QueryResults<DtoCashAccountTransactionOverview> loadOverview(
    		CashAccount cashAccountEntity,
		    PagingParams<CashAccountTransactionOverviewFields> pagingParams) {
    	
        Predicate predicate = calculatePredicate(cashAccountEntity, pagingParams);

        return queryUtil.createPagedQuery(pagingParams, CashAccountTransactionOverviewFields.VALUTA_DATE, Order.DESC)
                .select(Projections.bean(DtoCashAccountTransactionOverview.class,
                        cashAccountTransaction.id,
		                ExpressionUtils.as(Projections.bean(DtoCategory.class, category.id, category.name, binaryData.rawData.as("icon")).skipNulls(), "category"),
		                Projections.bean(Money.class, cashAccountTransaction.value, cashAccount.currency).as("valueWithCurrency"),
		                Projections.bean(Money.class, cashAccountTransaction.saldoBefore.as("value"), cashAccount.currency).as("saldoBefore"),
		                Projections.bean(Money.class, cashAccountTransaction.saldoAfter.as("value"), cashAccount.currency).as("saldoAfter"),
                        cashAccountTransaction.usage, cashAccountTransaction.description,
                        cashAccountTransaction.valutaDate, cashAccountTransaction.value,
                        cashAccountTransaction.reference, cashAccountTransaction.referenceIban,
                        cashAccountTransaction.referenceBank, cashAccountTransaction.creditorIdentifier,
                        cashAccountTransaction.mandate, cashAccountTransaction.creationDate))
                .from(cashAccountTransaction)
                .innerJoin(cashAccountTransaction.cashAccount(), cashAccount)
                .leftJoin(cashAccountTransaction.category(), category)
                .leftJoin(category.icon(), binaryData)
                .where(predicate)
		        .orderBy(new OrderSpecifier(Order.DESC, cashAccountTransaction.creationDate, NullsLast))
		        .orderBy(new OrderSpecifier(Order.DESC, cashAccountTransaction.id, NullsLast))
                .fetchResults();
    }

    private Predicate calculatePredicate(CashAccount cashAccount, PagingParams<CashAccountTransactionOverviewFields> pagingParams) {
        String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";

        Predicate predicate = cashAccountTransaction.usage.likeIgnoreCase(filter)
                .or(cashAccountTransaction.description.likeIgnoreCase(filter))
                .or(category.name.likeIgnoreCase(filter));

        predicate = ExpressionUtils.allOf(predicate,
                cashAccountTransaction.deletionDate.isNull(),
                cashAccountTransaction.cashAccount().eq(cashAccount));

        return predicate;
    }
}
