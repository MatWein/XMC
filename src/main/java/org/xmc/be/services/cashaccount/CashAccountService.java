package org.xmc.be.services.cashaccount;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import org.xmc.be.repositories.cashaccount.CashAccountRepository;
import org.xmc.be.services.cashaccount.controller.CashAccountSaveController;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import org.xmc.common.stubs.cashaccount.DtoCashAccount;
import org.xmc.common.stubs.cashaccount.DtoCashAccountOverview;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class CashAccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountService.class);

    private final CashAccountRepository cashAccountRepository;
    private final CashAccountSaveController cashAccountSaveController;
    private final CashAccountJpaRepository cashAccountJpaRepository;

    @Autowired
    public CashAccountService(
            CashAccountRepository cashAccountRepository,
            CashAccountSaveController cashAccountSaveController,
            CashAccountJpaRepository cashAccountJpaRepository) {

        this.cashAccountRepository = cashAccountRepository;
        this.cashAccountSaveController = cashAccountSaveController;
        this.cashAccountJpaRepository = cashAccountJpaRepository;
    }

    public void saveOrUpdate(AsyncMonitor monitor, DtoCashAccount dtoCashAccount) {
        LOGGER.info("Saving cash account: {}", dtoCashAccount);
        monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_CASHACCOUNT);

        cashAccountSaveController.saveOrUpdate(dtoCashAccount);
    }

    public QueryResults<DtoCashAccountOverview> loadOverview(AsyncMonitor monitor, PagingParams<CashAccountOverviewFields> pagingParams) {
        LOGGER.info("Loading cash account overview: {}", pagingParams);
        monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_CASHACCOUNT_OVERVIEW);

        return cashAccountRepository.loadOverview(pagingParams);
    }

    public void markAsDeleted(AsyncMonitor monitor, Long cashAccountId) {
        LOGGER.info("Marking cash account '{}' as deleted.", cashAccountId);
        monitor.setStatusText(MessageKey.ASYNC_TASK_DELETE_CASHACCOUNT);

        Optional<CashAccount> cashAccount = cashAccountJpaRepository.findById(cashAccountId);
        if (cashAccount.isPresent()) {
            cashAccount.get().setDeletionDate(LocalDateTime.now());
            cashAccountJpaRepository.save(cashAccount.get());
        }
    }
}
