package io.github.matwein.xmc.be.services.bank;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.be.repositories.bank.BankJpaRepository;
import io.github.matwein.xmc.be.repositories.bank.BankRepository;
import io.github.matwein.xmc.be.services.bank.controller.BankSaveController;
import io.github.matwein.xmc.be.services.bank.mapper.BankToDtoBankMapper;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.bank.BankOverviewFields;
import io.github.matwein.xmc.common.stubs.bank.DtoBank;
import io.github.matwein.xmc.common.stubs.bank.DtoBankOverview;
import io.github.matwein.xmc.fe.async.AsyncMonitor;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BankService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankService.class);

    private final BankJpaRepository bankJpaRepository;
    private final BankToDtoBankMapper bankToDtoBankMapper;
    private final BankRepository bankRepository;
    private final BankSaveController bankSaveController;

    @Autowired
    public BankService(
            BankJpaRepository bankJpaRepository,
            BankToDtoBankMapper bankToDtoBankMapper,
            BankRepository bankRepository,
            BankSaveController bankSaveController) {

        this.bankJpaRepository = bankJpaRepository;
        this.bankToDtoBankMapper = bankToDtoBankMapper;
        this.bankRepository = bankRepository;
        this.bankSaveController = bankSaveController;
    }

    public List<DtoBank> loadAllBanks(AsyncMonitor monitor) {
        LOGGER.info("Loading all available banks.");
        monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_ALL_BANKS);

        return bankJpaRepository.findByDeletionDateIsNull().stream()
                .map(bankToDtoBankMapper)
                .collect(Collectors.toList());
    }

    public void saveOrUpdate(AsyncMonitor monitor, DtoBank dtoBank) {
        LOGGER.info("Saving bank: {}", dtoBank);
        monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_BANK);

        bankSaveController.saveOrUpdate(dtoBank);
    }

    public QueryResults<DtoBankOverview> loadOverview(AsyncMonitor monitor, PagingParams<BankOverviewFields> pagingParams) {
        LOGGER.info("Loading bank overview: {}", pagingParams);
        monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_BANK_OVERVIEW);

        return bankRepository.loadOverview(pagingParams);
    }

    public void markAsDeleted(AsyncMonitor monitor, long bankId) {
        LOGGER.info("Marking bank '{}' as deleted.", bankId);
        monitor.setStatusText(MessageKey.ASYNC_TASK_DELETE_BANK);

        Bank bank = bankJpaRepository.getOne(bankId);
        bank.setDeletionDate(LocalDateTime.now());
        bankJpaRepository.save(bank);
    }
}
