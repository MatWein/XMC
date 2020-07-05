package org.xmc.be.services.cashaccount;

import com.querydsl.core.QueryResults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import org.xmc.common.stubs.cashaccount.DtoCashAccount;

@Service
@Transactional
public class CashAccountService {
    public void saveOrUpdate(DtoCashAccount dtoCashAccount) {

    }

    public QueryResults<DtoCashAccount> loadOverview(PagingParams<CashAccountOverviewFields> pagingParams) {
        System.out.println(pagingParams);
        return QueryResults.emptyResults();
    }
}
