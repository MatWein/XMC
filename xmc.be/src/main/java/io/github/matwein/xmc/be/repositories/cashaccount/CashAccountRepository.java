package io.github.matwein.xmc.be.repositories.cashaccount;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.github.matwein.xmc.be.common.QueryUtil;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.DtoCashAccountOverview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static io.github.matwein.xmc.be.entities.QBank.bank;
import static io.github.matwein.xmc.be.entities.QBinaryData.binaryData;
import static io.github.matwein.xmc.be.entities.cashaccount.QCashAccount.cashAccount;

@Repository
public class CashAccountRepository {
    private final QueryUtil queryUtil;

    @Autowired
    public CashAccountRepository(QueryUtil queryUtil) {
        this.queryUtil = queryUtil;
    }

    public QueryResults<DtoCashAccountOverview> loadOverview(PagingParams<CashAccountOverviewFields> pagingParams) {
        String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
        BooleanExpression predicate = cashAccount.name.likeIgnoreCase(filter)
                        .or(cashAccount.number.likeIgnoreCase(filter))
                        .or(cashAccount.iban.likeIgnoreCase(filter))
                        .or(bank.bic.likeIgnoreCase(filter))
                        .or(bank.blz.likeIgnoreCase(filter))
                        .or(bank.name.likeIgnoreCase(filter));

        return queryUtil.createPagedQuery(pagingParams, CashAccountOverviewFields.NAME, Order.ASC)
                .select(Projections.constructor(DtoCashAccountOverview.class,
                        cashAccount.id, cashAccount.iban, cashAccount.number, cashAccount.name, cashAccount.currency, cashAccount.color,
                        cashAccount.creationDate, cashAccount.lastSaldo, cashAccount.lastSaldoDate,
                        bank.id, bank.name, bank.bic, bank.blz,
                        binaryData.rawData))
                .from(cashAccount)
                .innerJoin(cashAccount.bank(), bank)
                .leftJoin(bank.logo(), binaryData)
                .where(ExpressionUtils.allOf(predicate, cashAccount.deletionDate.isNull()))
                .fetchResults();
    }
}
