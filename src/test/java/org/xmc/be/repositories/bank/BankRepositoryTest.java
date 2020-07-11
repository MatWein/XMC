package org.xmc.be.repositories.bank;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.be.entities.Bank;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.bank.BankOverviewFields;
import org.xmc.common.stubs.bank.DtoBankOverview;

class BankRepositoryTest extends IntegrationTest {
    @Autowired
    private BankRepository repository;

    @Test
    void testLoadOverview() {
        PagingParams<BankOverviewFields> pagingParams = new PagingParams<>(0, 3, BankOverviewFields.BANK_BIC, Order.DESC, null);

        Bank bank1 = graphGenerator.createBank();
        bank1.setBic("BIC Bank A");
        session().saveOrUpdate(bank1);

        Bank bank2 = graphGenerator.createBank();
        bank2.setBic("BIC Bank D");
        session().saveOrUpdate(bank2);

        Bank bank3 = graphGenerator.createBank();
        bank3.setBic("BIC Bank B");
        session().saveOrUpdate(bank3);

        Bank bank4 = graphGenerator.createBank();
        bank4.setBic("BIC Bank C");
        session().saveOrUpdate(bank4);

        Bank bank5 = graphGenerator.createBank();
        bank5.setBic("BIC Bank E");
        session().saveOrUpdate(bank5);

        flushAndClear();

        QueryResults<DtoBankOverview> result = repository.loadOverview(pagingParams);

        Assert.assertEquals(5, result.getTotal());
        Assert.assertEquals(3, result.getResults().size());
        Assert.assertEquals(bank5.getId(), result.getResults().get(0).getId());
        Assert.assertEquals(bank2.getId(), result.getResults().get(1).getId());
        Assert.assertEquals(bank4.getId(), result.getResults().get(2).getId());
    }

    @Test
    void testLoadOverview_Filter() {
        PagingParams<BankOverviewFields> pagingParams = new PagingParams<>(0, 3, BankOverviewFields.BANK_BIC, Order.DESC, "BIC Bank A");

        Bank bank1 = graphGenerator.createBank();
        bank1.setBic("BIC Bank A");
        session().saveOrUpdate(bank1);

        Bank bank2 = graphGenerator.createBank();
        bank2.setBic("BIC Bank D");
        session().saveOrUpdate(bank2);

        Bank bank3 = graphGenerator.createBank();
        bank3.setBic("BIC Bank B");
        session().saveOrUpdate(bank3);

        Bank bank4 = graphGenerator.createBank();
        bank4.setBic("BIC Bank C");
        session().saveOrUpdate(bank4);

        Bank bank5 = graphGenerator.createBank();
        bank5.setBic("BIC Bank E");
        session().saveOrUpdate(bank5);

        flushAndClear();

        QueryResults<DtoBankOverview> result = repository.loadOverview(pagingParams);

        Assert.assertEquals(1, result.getTotal());
        Assert.assertEquals(1, result.getResults().size());
        Assert.assertEquals(bank1.getId(), result.getResults().get(0).getId());
    }
}