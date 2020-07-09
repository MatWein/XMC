package org.xmc.be.services.cashaccount;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.repositories.cashaccount.CashAccountRepository;
import org.xmc.be.services.cashaccount.controller.CashAccountSaveController;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import org.xmc.common.stubs.cashaccount.DtoCashAccount;
import org.xmc.common.stubs.cashaccount.DtoCashAccountOverview;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

@Service
@Transactional
public class CashAccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountService.class);

    private final CashAccountRepository cashAccountRepository;
    private final CashAccountSaveController cashAccountSaveController;

    @Autowired
    public CashAccountService(
            CashAccountRepository cashAccountRepository,
            CashAccountSaveController cashAccountSaveController) {

        this.cashAccountRepository = cashAccountRepository;
        this.cashAccountSaveController = cashAccountSaveController;
    }

    public void saveOrUpdate(DtoCashAccount dtoCashAccount) {
        LOGGER.info("Saving cash account: {}", dtoCashAccount);
        cashAccountSaveController.saveOrUpdate(dtoCashAccount);
    }

    public QueryResults<DtoCashAccountOverview> loadOverview(AsyncMonitor monitor, PagingParams<CashAccountOverviewFields> pagingParams) {
        LOGGER.info("Loading cash account overview: {}", pagingParams);
        monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_CASHACCOUNT_OVERVIEW);

        return cashAccountRepository.loadOverview(pagingParams);
    }
}
