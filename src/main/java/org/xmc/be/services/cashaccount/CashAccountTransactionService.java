package org.xmc.be.services.cashaccount;

import com.querydsl.core.QueryResults;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import org.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import org.xmc.be.repositories.cashaccount.CashAccountTransactionRepository;
import org.xmc.be.services.cashaccount.controller.CashAccountTransactionSaldoUpdater;
import org.xmc.be.services.cashaccount.controller.CashAccountTransactionSaveController;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview;
import org.xmc.common.stubs.category.DtoCategory;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class CashAccountTransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountTransactionService.class);

    private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
    private final CashAccountTransactionRepository cashAccountTransactionRepository;
    private final CashAccountTransactionSaveController cashAccountTransactionSaveController;
    private final CashAccountJpaRepository cashAccountJpaRepository;
    private final CashAccountTransactionSaldoUpdater cashAccountTransactionSaldoUpdater;

    @Autowired
    public CashAccountTransactionService(
            CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository,
            CashAccountTransactionRepository cashAccountTransactionRepository,
            CashAccountTransactionSaveController cashAccountTransactionSaveController,
            CashAccountJpaRepository cashAccountJpaRepository,
            CashAccountTransactionSaldoUpdater cashAccountTransactionSaldoUpdater) {

        this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
        this.cashAccountTransactionRepository = cashAccountTransactionRepository;
        this.cashAccountTransactionSaveController = cashAccountTransactionSaveController;
        this.cashAccountJpaRepository = cashAccountJpaRepository;
        this.cashAccountTransactionSaldoUpdater = cashAccountTransactionSaldoUpdater;
    }

    public QueryResults<DtoCashAccountTransactionOverview> loadOverview(
            AsyncMonitor monitor,
            long cashAccountId,
            PagingParams<CashAccountTransactionOverviewFields> pagingParams) {

        LOGGER.info("Loading cash account transaction overview: {}", pagingParams);
        monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_CASHACCOUNT_TRANSACTION_OVERVIEW);

        CashAccount cashAccount = cashAccountJpaRepository.getOne(cashAccountId);
        return cashAccountTransactionRepository.loadOverview(cashAccount, pagingParams);
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

    public void saveOrUpdate(AsyncMonitor monitor, long cashAccountId, DtoCashAccountTransaction dtoCashAccountTransaction) {
        LOGGER.info("Saving cash account transaction: {}", dtoCashAccountTransaction);
        monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_CASHACCOUNT_TRANSACTION);

        CashAccount cashAccount = cashAccountJpaRepository.getOne(cashAccountId);
        cashAccountTransactionSaveController.saveOrUpdate(cashAccount, dtoCashAccountTransaction);
    }

    public Optional<DtoCategory> autoDetectCategory(
            AsyncMonitor monitor,
            ObservableList<DtoCategory> items,
            String usage) {

        LOGGER.info("Auto detecting cash account transaction category...");
        monitor.setStatusText(MessageKey.ASYNC_TASK_DETECT_CASHACCOUNT_TRANSACTION_CATEGORY);



        return Optional.empty();
    }

    public Pair<BigDecimal, BigDecimal> calculateSaldoPreview(LocalDate valutaDate, BigDecimal value) {
        BigDecimal saldoBefore = cashAccountTransactionSaldoUpdater.calculateSaldoBefore(valutaDate);
        BigDecimal saldoAfter = cashAccountTransactionSaldoUpdater.calculateSaldoAfter(saldoBefore, value);

        return ImmutablePair.of(saldoBefore, saldoAfter);
    }
}
