package io.github.matwein.xmc.be.services.bank;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.be.repositories.bank.BankJpaRepository;
import io.github.matwein.xmc.be.repositories.bank.BankRepository;
import io.github.matwein.xmc.be.services.bank.controller.BankSaveController;
import io.github.matwein.xmc.be.services.bank.mapper.BankToDtoBankMapper;
import io.github.matwein.xmc.common.services.bank.IBankService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.bank.BankOverviewFields;
import io.github.matwein.xmc.common.stubs.bank.DtoBank;
import io.github.matwein.xmc.common.stubs.bank.DtoBankOverview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BankService implements IBankService {
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

    @Override
    public List<DtoBank> loadAllBanks(IAsyncMonitor monitor) {
        LOGGER.info("Loading all available banks.");
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_ALL_BANKS));

        return bankJpaRepository.findByDeletionDateIsNull().stream()
                .map(bankToDtoBankMapper)
                .collect(Collectors.toList());
    }
	
	@Override
    public void saveOrUpdate(IAsyncMonitor monitor, DtoBank dtoBank) {
        LOGGER.info("Saving bank: {}", dtoBank);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_BANK));

        bankSaveController.saveOrUpdate(dtoBank);
    }
	
	@Override
    public QueryResults<DtoBankOverview> loadOverview(IAsyncMonitor monitor, PagingParams<BankOverviewFields> pagingParams) {
        LOGGER.info("Loading bank overview: {}", pagingParams);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_BANK_OVERVIEW));

        return bankRepository.loadOverview(pagingParams);
    }
	
	@Override
    public void markAsDeleted(IAsyncMonitor monitor, long bankId) {
        LOGGER.info("Marking bank '{}' as deleted.", bankId);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_DELETE_BANK));

        Bank bank = bankJpaRepository.getOne(bankId);
        bank.setDeletionDate(LocalDateTime.now());
        bankJpaRepository.save(bank);
    }
}
