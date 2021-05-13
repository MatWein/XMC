package io.github.matwein.xmc.be.repositories.bank;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.bank.BankOverviewFields;
import io.github.matwein.xmc.common.stubs.bank.DtoBankOverview;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

        Assertions.assertEquals(5, result.getTotal());
        Assertions.assertEquals(3, result.getResults().size());
        Assertions.assertEquals(bank5.getId(), result.getResults().get(0).getId());
        Assertions.assertEquals(bank2.getId(), result.getResults().get(1).getId());
        Assertions.assertEquals(bank4.getId(), result.getResults().get(2).getId());
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

        Assertions.assertEquals(1, result.getTotal());
        Assertions.assertEquals(1, result.getResults().size());
        Assertions.assertEquals(bank1.getId(), result.getResults().get(0).getId());
    }
}