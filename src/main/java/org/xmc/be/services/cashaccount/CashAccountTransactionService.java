package org.xmc.be.services.cashaccount;

import com.querydsl.core.QueryResults;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import org.xmc.be.repositories.cashaccount.CashAccountTransactionRepository;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview;
import org.xmc.common.stubs.category.DtoCategory;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class CashAccountTransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountTransactionService.class);

    private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
    private final CashAccountTransactionRepository cashAccountTransactionRepository;

    @Autowired
    public CashAccountTransactionService(
            CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository,
            CashAccountTransactionRepository cashAccountTransactionRepository) {

        this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
        this.cashAccountTransactionRepository = cashAccountTransactionRepository;
    }

    public QueryResults<DtoCashAccountTransactionOverview> loadOverview(
            AsyncMonitor monitor,
            PagingParams<CashAccountTransactionOverviewFields> pagingParams) {

        LOGGER.info("Loading cash account transaction overview: {}", pagingParams);
        monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_CASHACCOUNT_TRANSACTION_OVERVIEW);

        return cashAccountTransactionRepository.loadOverview(pagingParams);
    }

    public void markAsDeleted(AsyncMonitor monitor, Long transactionId) {
        LOGGER.info("Marking cash account transaction '{}' as deleted.", transactionId);
        monitor.setStatusText(MessageKey.ASYNC_TASK_DELETE_CASHACCOUNT_TRANSACTION);

        Optional<CashAccountTransaction> transaction = cashAccountTransactionJpaRepository.findById(transactionId);
        if (transaction.isPresent()) {
            transaction.get().setDeletionDate(LocalDateTime.now());
            cashAccountTransactionJpaRepository.save(transaction.get());
        }
    }

    public void saveOrUpdate(AsyncMonitor monitor, DtoCashAccountTransaction dtoCashAccountTransaction) {

    }

    public Optional<DtoCategory> autoDetectCategory(AsyncMonitor monitor, ObservableList<DtoCategory> items, String usage) {
        return Optional.empty();
    }
}
