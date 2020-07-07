package org.xmc.be.repositories.cashaccount;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmc.be.common.QueryUtil;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import org.xmc.common.stubs.cashaccount.DtoCashAccount;

import static org.xmc.be.entities.QBank.bank;
import static org.xmc.be.entities.QBinaryData.binaryData;
import static org.xmc.be.entities.cashaccount.QCashAccount.cashAccount;

@Repository
public class CashAccountRepository {
    private final QueryUtil queryUtil;

    @Autowired
    public CashAccountRepository(QueryUtil queryUtil) {
        this.queryUtil = queryUtil;
    }

    public QueryResults<DtoCashAccount> loadOverview(PagingParams<CashAccountOverviewFields> pagingParams) {
        String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
        BooleanExpression predicate = cashAccount.name.likeIgnoreCase(filter)
                        .or(cashAccount.number.likeIgnoreCase(filter))
                        .or(cashAccount.iban.likeIgnoreCase(filter))
                        .or(cashAccount.bank().bic.likeIgnoreCase(filter))
                        .or(cashAccount.bank().blz.likeIgnoreCase(filter))
                        .or(cashAccount.bank().name.likeIgnoreCase(filter));

        return queryUtil.createPagedQuery(pagingParams)
                .select(Projections.constructor(DtoCashAccount.class,
                        cashAccount.id, cashAccount.iban, cashAccount.number, cashAccount.name, cashAccount.currency,
                        bank.id, bank.name, bank.bic, bank.blz,
                        binaryData.rawData))
                .from(cashAccount)
                .innerJoin(cashAccount.bank(), bank)
                .leftJoin(bank.logo(), binaryData)
                .where(ExpressionUtils.allOf(predicate))
                .fetchResults();
    }
}