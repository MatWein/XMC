package org.xmc.be.repositories.bank;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmc.be.common.QueryUtil;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.bank.BankOverviewFields;
import org.xmc.common.stubs.bank.DtoBankOverview;

import static org.xmc.be.entities.QBank.bank;
import static org.xmc.be.entities.QBinaryData.binaryData;

@Repository
public class BankRepository {
    private final QueryUtil queryUtil;

    @Autowired
    public BankRepository(QueryUtil queryUtil) {
        this.queryUtil = queryUtil;
    }

    public QueryResults<DtoBankOverview> loadOverview(PagingParams<BankOverviewFields> pagingParams) {
        String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
        BooleanExpression predicate = bank.bic.likeIgnoreCase(filter)
                .or(bank.blz.likeIgnoreCase(filter))
                .or(bank.name.likeIgnoreCase(filter));

        return queryUtil.createPagedQuery(pagingParams)
                .select(Projections.constructor(DtoBankOverview.class,
                        bank.id, bank.name, bank.bic, bank.blz,
                        binaryData.rawData, bank.creationDate))
                .from(bank)
                .leftJoin(bank.logo(), binaryData)
                .where(ExpressionUtils.allOf(predicate))
                .fetchResults();
    }
}
