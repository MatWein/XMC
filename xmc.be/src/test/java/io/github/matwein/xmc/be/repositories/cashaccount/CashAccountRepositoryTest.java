package io.github.matwein.xmc.be.repositories.cashaccount;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.be.entities.BinaryData;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.DtoCashAccountOverview;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
class CashAccountRepositoryTest extends IntegrationTest {
    @Autowired
    private CashAccountRepository repository;

    @Test
    void testLoadOverview() {
        PagingParams<CashAccountOverviewFields> pagingParams = new PagingParams<>(0, 3, CashAccountOverviewFields.NAME, Order.ASC, null);

        CashAccount cashAccount1 = graphGenerator.createCashAccount();
        cashAccount1.setName("Bbb");
        session().saveOrUpdate(cashAccount1);

        CashAccount cashAccount2 = graphGenerator.createCashAccount();
        cashAccount2.setName("Aaa");
        session().saveOrUpdate(cashAccount2);

        CashAccount cashAccount3 = graphGenerator.createCashAccount();
        cashAccount3.setName("Ccc");
        session().saveOrUpdate(cashAccount3);

        CashAccount cashAccount4 = graphGenerator.createCashAccount();
        cashAccount4.setName("Zzz");
        session().saveOrUpdate(cashAccount4);

        CashAccount cashAccount5 = graphGenerator.createCashAccount();
        cashAccount5.setName("Zzz");
        session().saveOrUpdate(cashAccount5);

        flushAndClear();

        QueryResults<DtoCashAccountOverview> result = repository.loadOverview(pagingParams);

        Assertions.assertEquals(5, result.getTotal());
        Assertions.assertEquals(3, result.getResults().size());
        Assertions.assertEquals(cashAccount2.getId(), result.getResults().get(0).getId());
        Assertions.assertEquals(cashAccount1.getId(), result.getResults().get(1).getId());
        Assertions.assertEquals(cashAccount3.getId(), result.getResults().get(2).getId());
    }

    @Test
    void testLoadOverview_Filter() {
        PagingParams<CashAccountOverviewFields> pagingParams = new PagingParams<>(0, 3, CashAccountOverviewFields.IBAN, Order.ASC, "Filter");

        CashAccount cashAccount1 = graphGenerator.createCashAccount();
        cashAccount1.setName("Bbb");
        cashAccount1.setIban("IBAN Filter 1");
        session().saveOrUpdate(cashAccount1);

        CashAccount cashAccount2 = graphGenerator.createCashAccount();
        cashAccount2.setName("Aaa");
        cashAccount2.setIban("IBAN Filter 2");
        session().saveOrUpdate(cashAccount2);

        CashAccount cashAccount3 = graphGenerator.createCashAccount();
        cashAccount3.setName("Ccc");
        session().saveOrUpdate(cashAccount3);

        CashAccount cashAccount4 = graphGenerator.createCashAccount();
        cashAccount4.setName("Zzz");
        session().saveOrUpdate(cashAccount4);

        CashAccount cashAccount5 = graphGenerator.createCashAccount();
        cashAccount5.setName("Zzz");
        session().saveOrUpdate(cashAccount5);

        flushAndClear();

        QueryResults<DtoCashAccountOverview> result = repository.loadOverview(pagingParams);

        Assertions.assertEquals(2, result.getTotal());
        Assertions.assertEquals(2, result.getResults().size());
        Assertions.assertEquals(cashAccount1.getId(), result.getResults().get(0).getId());
        Assertions.assertEquals(cashAccount2.getId(), result.getResults().get(1).getId());
    }

    @Test
    void testLoadOverview_IgnoreDeleted() {
        PagingParams<CashAccountOverviewFields> pagingParams = new PagingParams<>(0, 3, null, null, null);

        CashAccount cashAccount1 = graphGenerator.createCashAccount();
        cashAccount1.setDeletionDate(LocalDateTime.now());
        session().saveOrUpdate(cashAccount1);

        flushAndClear();

        QueryResults<DtoCashAccountOverview> result = repository.loadOverview(pagingParams);

        Assertions.assertEquals(0, result.getTotal());
        Assertions.assertEquals(0, result.getResults().size());
    }

    @Test
    void testLoadOverview_Logo() {
        PagingParams<CashAccountOverviewFields> pagingParams = new PagingParams<>(0, 3, null, null, null);

        Bank bank = graphGenerator.createBank();
        BinaryData logo = graphGenerator.createBinaryData(bank);
        graphGenerator.createCashAccount(bank);

        flushAndClear();

        QueryResults<DtoCashAccountOverview> result = repository.loadOverview(pagingParams);

        Assertions.assertEquals(1, result.getTotal());
        Assertions.assertEquals(1, result.getResults().size());
        Assertions.assertEquals(bank.getId(), result.getResults().get(0).getBank().getId());
        Assertions.assertEquals(bank.getName(), result.getResults().get(0).getBank().getName());
        Assertions.assertEquals(bank.getBlz(), result.getResults().get(0).getBank().getBlz());
        Assertions.assertEquals(bank.getBic(), result.getResults().get(0).getBank().getBic());
        Assertions.assertArrayEquals(logo.getRawData(), result.getResults().get(0).getBank().getLogo());
    }
}