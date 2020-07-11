package org.xmc.be.repositories.cashaccount;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.IntegrationTest;
import org.xmc.be.entities.Bank;
import org.xmc.be.entities.BinaryData;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import org.xmc.common.stubs.cashaccount.DtoCashAccountOverview;

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

        Assert.assertEquals(5, result.getTotal());
        Assert.assertEquals(3, result.getResults().size());
        Assert.assertEquals(cashAccount2.getId(), result.getResults().get(0).getId());
        Assert.assertEquals(cashAccount1.getId(), result.getResults().get(1).getId());
        Assert.assertEquals(cashAccount3.getId(), result.getResults().get(2).getId());
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

        Assert.assertEquals(2, result.getTotal());
        Assert.assertEquals(2, result.getResults().size());
        Assert.assertEquals(cashAccount1.getId(), result.getResults().get(0).getId());
        Assert.assertEquals(cashAccount2.getId(), result.getResults().get(1).getId());
    }

    @Test
    void testLoadOverview_IgnoreDeleted() {
        PagingParams<CashAccountOverviewFields> pagingParams = new PagingParams<>(0, 3, null, null, null);

        CashAccount cashAccount1 = graphGenerator.createCashAccount();
        cashAccount1.setDeletionDate(LocalDateTime.now());
        session().saveOrUpdate(cashAccount1);

        flushAndClear();

        QueryResults<DtoCashAccountOverview> result = repository.loadOverview(pagingParams);

        Assert.assertEquals(0, result.getTotal());
        Assert.assertEquals(0, result.getResults().size());
    }

    @Test
    void testLoadOverview_Logo() {
        PagingParams<CashAccountOverviewFields> pagingParams = new PagingParams<>(0, 3, null, null, null);

        Bank bank = graphGenerator.createBank();
        BinaryData logo = graphGenerator.createBinaryData(bank);
        graphGenerator.createCashAccount(bank);

        flushAndClear();

        QueryResults<DtoCashAccountOverview> result = repository.loadOverview(pagingParams);

        Assert.assertEquals(1, result.getTotal());
        Assert.assertEquals(1, result.getResults().size());
        Assert.assertEquals(bank.getId(), result.getResults().get(0).getBank().getId());
        Assert.assertEquals(bank.getName(), result.getResults().get(0).getBank().getName());
        Assert.assertEquals(bank.getBlz(), result.getResults().get(0).getBank().getBlz());
        Assert.assertEquals(bank.getBic(), result.getResults().get(0).getBank().getBic());
        Assert.assertArrayEquals(logo.getRawData(), result.getResults().get(0).getBank().getLogo());
    }
}