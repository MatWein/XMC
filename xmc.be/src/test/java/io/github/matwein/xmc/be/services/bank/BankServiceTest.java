package io.github.matwein.xmc.be.services.bank;

import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.JUnitTestBase;
import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.be.repositories.bank.BankJpaRepository;
import io.github.matwein.xmc.be.repositories.bank.BankRepository;
import io.github.matwein.xmc.be.services.bank.controller.BankSaveController;
import io.github.matwein.xmc.be.services.bank.mapper.BankToDtoBankMapper;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.bank.BankOverviewFields;
import io.github.matwein.xmc.common.stubs.bank.DtoBank;
import io.github.matwein.xmc.common.stubs.bank.DtoBankOverview;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BankServiceTest extends JUnitTestBase {
    private BankService service;

    @Mock private BankJpaRepository bankJpaRepository;
    @Mock private BankToDtoBankMapper bankToDtoBankMapper;
    @Mock private BankRepository bankRepository;
    @Mock private BankSaveController bankSaveController;

    @BeforeEach
    void setUp() {
        service = new BankService(bankJpaRepository, bankToDtoBankMapper, bankRepository, bankSaveController);
    }

    @Test
    void testLoadAllBanks() {
        IAsyncMonitor monitor = Mockito.mock(IAsyncMonitor.class);

        Bank bank = new Bank();
        List<Bank> banks = Lists.newArrayList(bank);
        when(bankJpaRepository.findByDeletionDateIsNull()).thenReturn(banks);

        DtoBank dtoBank = new DtoBank();
        when(bankToDtoBankMapper.apply(bank)).thenReturn(dtoBank);

        List<DtoBank> result = service.loadAllBanks(monitor);

        verify(monitor).setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_ALL_BANKS));
        verify(bankToDtoBankMapper).apply(bank);

        Assertions.assertEquals(Lists.newArrayList(dtoBank), result);
    }

    @Test
    void testSaveOrUpdate() {
        IAsyncMonitor monitor = Mockito.mock(IAsyncMonitor.class);
        DtoBank dtoBank = new DtoBank();

        service.saveOrUpdate(monitor, dtoBank);

        verify(bankSaveController).saveOrUpdate(dtoBank);
        verify(monitor).setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_BANK));
    }

    @Test
    void testLoadOverview() {
        IAsyncMonitor monitor = Mockito.mock(IAsyncMonitor.class);
        PagingParams<BankOverviewFields> pagingParams = new PagingParams<>();

        QueryResults<DtoBankOverview> expectedResult = QueryResults.emptyResults();
        when(bankRepository.loadOverview(pagingParams)).thenReturn(expectedResult);

        QueryResults<DtoBankOverview> result = service.loadOverview(monitor, pagingParams);

        verify(monitor).setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_BANK_OVERVIEW));
        verify(bankRepository).loadOverview(pagingParams);

        Assertions.assertSame(expectedResult, result);
    }
}