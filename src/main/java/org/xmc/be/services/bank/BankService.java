package org.xmc.be.services.bank;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.repositories.bank.BankJpaRepository;
import org.xmc.be.repositories.bank.BankRepository;
import org.xmc.be.services.bank.mapper.BankToDtoBankMapper;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.bank.BankOverviewFields;
import org.xmc.common.stubs.bank.DtoBank;
import org.xmc.common.stubs.bank.DtoBankOverview;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BankService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankService.class);

    private final BankJpaRepository bankJpaRepository;
    private final BankToDtoBankMapper bankToDtoBankMapper;
    private final BankRepository bankRepository;

    @Autowired
    public BankService(
            BankJpaRepository bankJpaRepository,
            BankToDtoBankMapper bankToDtoBankMapper,
            BankRepository bankRepository) {

        this.bankJpaRepository = bankJpaRepository;
        this.bankToDtoBankMapper = bankToDtoBankMapper;
        this.bankRepository = bankRepository;
    }

    public List<DtoBank> loadAllBanks() {
        LOGGER.info("Loading all available banks.");

        return bankJpaRepository.findAll().stream()
                .map(bankToDtoBankMapper)
                .collect(Collectors.toList());
    }

    public void saveOrUpdate(DtoBank dtoBank) {
        LOGGER.info("Saving bank: {}", dtoBank);
//        cashAccountSaveController.saveOrUpdate(dtoCashAccount);
    }

    public QueryResults<DtoBankOverview> loadOverview(AsyncMonitor monitor, PagingParams<BankOverviewFields> pagingParams) {
        LOGGER.info("Loading bank overview: {}", pagingParams);
        monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_BANK_OVERVIEW);

        return bankRepository.loadOverview(pagingParams);
    }
}
