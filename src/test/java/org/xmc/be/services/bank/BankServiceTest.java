package org.xmc.be.services.bank;

import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.xmc.JUnitTestBase;
import org.xmc.be.entities.Bank;
import org.xmc.be.repositories.bank.BankJpaRepository;
import org.xmc.be.repositories.bank.BankRepository;
import org.xmc.be.services.bank.controller.BankSaveController;
import org.xmc.be.services.bank.mapper.BankToDtoBankMapper;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.bank.BankOverviewFields;
import org.xmc.common.stubs.bank.DtoBank;
import org.xmc.common.stubs.bank.DtoBankOverview;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

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
        AsyncMonitor monitor = Mockito.mock(AsyncMonitor.class);

        Bank bank = testObjectFactory.create(Bank.class);
        List<Bank> banks = Lists.newArrayList(bank);
        when(bankJpaRepository.findByDeletionDateIsNull()).thenReturn(banks);

        DtoBank dtoBank = new DtoBank();
        when(bankToDtoBankMapper.apply(bank)).thenReturn(dtoBank);

        List<DtoBank> result = service.loadAllBanks(monitor);

        verify(monitor).setStatusText(MessageKey.ASYNC_TASK_LOAD_ALL_BANKS);
        verify(bankToDtoBankMapper).apply(bank);

        Assert.assertEquals(Lists.newArrayList(dtoBank), result);
    }

    @Test
    void testSaveOrUpdate() {
        AsyncMonitor monitor = Mockito.mock(AsyncMonitor.class);
        DtoBank dtoBank = new DtoBank();

        service.saveOrUpdate(monitor, dtoBank);

        verify(bankSaveController).saveOrUpdate(dtoBank);
        verify(monitor).setStatusText(MessageKey.ASYNC_TASK_SAVE_BANK);
    }

    @Test
    void testLoadOverview() {
        AsyncMonitor monitor = Mockito.mock(AsyncMonitor.class);
        PagingParams<BankOverviewFields> pagingParams = new PagingParams<>();

        QueryResults<DtoBankOverview> expectedResult = QueryResults.emptyResults();
        when(bankRepository.loadOverview(pagingParams)).thenReturn(expectedResult);

        QueryResults<DtoBankOverview> result = service.loadOverview(monitor, pagingParams);

        verify(monitor).setStatusText(MessageKey.ASYNC_TASK_LOAD_BANK_OVERVIEW);
        verify(bankRepository).loadOverview(pagingParams);

        Assert.assertSame(expectedResult, result);
    }
}