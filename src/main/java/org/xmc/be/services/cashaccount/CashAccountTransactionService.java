package org.xmc.be.services.cashaccount;

import com.querydsl.core.QueryResults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview;
import org.xmc.fe.async.AsyncMonitor;

@Service
@Transactional
public class CashAccountTransactionService {
    public QueryResults<DtoCashAccountTransactionOverview> loadOverview(
            AsyncMonitor monitor,
            PagingParams<CashAccountTransactionOverviewFields> pagingParams) {

        return QueryResults.emptyResults();
    }

    public void markAsDeleted(AsyncMonitor monitor, Long transactionId) {

    }

    public void saveOrUpdate(AsyncMonitor monitor, DtoCashAccountTransaction dtoCashAccountTransaction) {

    }
}
