package io.github.matwein.xmc.be.services.cashaccount;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountTransactionRepository;
import io.github.matwein.xmc.be.services.cashaccount.controller.CashAccountTransactionSaldoUpdater;
import io.github.matwein.xmc.be.services.cashaccount.controller.CashAccountTransactionSaveController;
import io.github.matwein.xmc.be.services.cashaccount.controller.CategoryDetectionController;
import io.github.matwein.xmc.common.services.cashaccount.ICashAccountTransactionService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CashAccountTransactionService implements ICashAccountTransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountTransactionService.class);

    private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
    private final CashAccountTransactionRepository cashAccountTransactionRepository;
    private final CashAccountTransactionSaveController cashAccountTransactionSaveController;
    private final CashAccountJpaRepository cashAccountJpaRepository;
    private final CashAccountTransactionSaldoUpdater cashAccountTransactionSaldoUpdater;
    private final CategoryDetectionController categoryDetectionController;
	
	@Autowired
    public CashAccountTransactionService(
		    CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository,
		    CashAccountTransactionRepository cashAccountTransactionRepository,
		    CashAccountTransactionSaveController cashAccountTransactionSaveController,
		    CashAccountJpaRepository cashAccountJpaRepository,
		    CashAccountTransactionSaldoUpdater cashAccountTransactionSaldoUpdater,
		    CategoryDetectionController categoryDetectionController) {

        this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
        this.cashAccountTransactionRepository = cashAccountTransactionRepository;
        this.cashAccountTransactionSaveController = cashAccountTransactionSaveController;
        this.cashAccountJpaRepository = cashAccountJpaRepository;
        this.cashAccountTransactionSaldoUpdater = cashAccountTransactionSaldoUpdater;
        this.categoryDetectionController = categoryDetectionController;
	}

    @Override
    public QueryResults<DtoCashAccountTransactionOverview> loadOverview(
            IAsyncMonitor monitor,
            long cashAccountId,
            PagingParams<CashAccountTransactionOverviewFields> pagingParams) {

        LOGGER.info("Loading cash account transaction overview: {}", pagingParams);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_CASHACCOUNT_TRANSACTION_OVERVIEW));

        CashAccount cashAccount = cashAccountJpaRepository.getReferenceById(cashAccountId);
	    return cashAccountTransactionRepository.loadOverview(cashAccount, pagingParams);
    }
	
	@Override
    public void markAsDeleted(IAsyncMonitor monitor, Collection<Long> transactionIds) {
        LOGGER.info("Marking cash account transactions '{}' as deleted.", transactionIds);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_DELETE_CASHACCOUNT_TRANSACTIONS));

        List<CashAccountTransaction> transactions = cashAccountTransactionJpaRepository.findAllById(transactionIds);
        CashAccountTransaction oldestTransaction = null;

        for (CashAccountTransaction transaction : transactions) {
            transaction.setDeletionDate(LocalDateTime.now());
            cashAccountTransactionJpaRepository.save(transaction);

            if (oldestTransaction == null || transaction.getValutaDate().isBefore(oldestTransaction.getValutaDate())) {
                oldestTransaction = transaction;
            }
        }

        cashAccountTransactionSaldoUpdater.updateAll(oldestTransaction);
    }
	
	@Override
    public void saveOrUpdate(IAsyncMonitor monitor, long cashAccountId, DtoCashAccountTransaction dtoCashAccountTransaction) {
        LOGGER.info("Saving cash account transaction: {}", dtoCashAccountTransaction);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_CASHACCOUNT_TRANSACTION));

        CashAccount cashAccount = cashAccountJpaRepository.getReferenceById(cashAccountId);
        cashAccountTransactionSaveController.saveOrUpdate(cashAccount, dtoCashAccountTransaction);
    }
	
	@Override
    public Optional<Long> autoDetectCategory(
            IAsyncMonitor monitor,
            long cashAccountId,
            String usage) {

        LOGGER.info("Auto detecting cash account transaction category...");
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_DETECT_CASHACCOUNT_TRANSACTION_CATEGORY));

        CashAccount cashAccount = cashAccountJpaRepository.getReferenceById(cashAccountId);
        return categoryDetectionController.autoDetectCategory(cashAccount, usage);
    }
	
	@Override
    public Pair<BigDecimal, BigDecimal> calculateSaldoPreview(long cashAccountId, Long transactionId, LocalDate valutaDate, BigDecimal value) {
        CashAccount cashAccount = cashAccountJpaRepository.getReferenceById(cashAccountId);

        BigDecimal saldoBefore;
        if (transactionId == null) {
            saldoBefore = cashAccountTransactionJpaRepository.findFirstTransactionBeforeOrOnDate(cashAccount, valutaDate, LocalDateTime.now(), Long.MAX_VALUE)
                    .map(CashAccountTransaction::getSaldoAfter)
                    .orElse(BigDecimal.valueOf(0.0));
        } else {
            CashAccountTransaction transaction = cashAccountTransactionJpaRepository.getReferenceById(transactionId);
            saldoBefore = cashAccountTransactionJpaRepository.findFirstTransactionBeforeOrOnDate(cashAccount, valutaDate, transaction.getCreationDate(), transaction.getId())
                    .map(CashAccountTransaction::getSaldoAfter)
                    .orElse(BigDecimal.valueOf(0.0));
        }

        BigDecimal saldoAfter = cashAccountTransactionSaldoUpdater.calculateSaldoAfter(saldoBefore, value);

        return ImmutablePair.of(saldoBefore, saldoAfter);
    }
}
