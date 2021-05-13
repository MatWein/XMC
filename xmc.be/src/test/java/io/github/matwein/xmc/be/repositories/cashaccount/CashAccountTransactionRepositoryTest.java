package io.github.matwein.xmc.be.repositories.cashaccount;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

class CashAccountTransactionRepositoryTest extends IntegrationTest {
    @Autowired
    private CashAccountTransactionRepository repository;

    @Test
    void testLoadOverview() {
        PagingParams<CashAccountTransactionOverviewFields> pagingParams = new PagingParams<>();
        pagingParams.setOffset(0);
        pagingParams.setLimit(2);
        pagingParams.setSortBy(CashAccountTransactionOverviewFields.VALUTA_DATE);
        pagingParams.setOrder(Order.DESC);

        CashAccount cashAccount = graphGenerator.createCashAccount();

        graphGenerator.createCashAccountTransaction();

        var cashAccountTransaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction1.setValutaDate(LocalDate.of(2020, Month.AUGUST, 1));
        session().saveOrUpdate(cashAccountTransaction1);

        var cashAccountTransaction2 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction2.setValutaDate(LocalDate.of(2020, Month.AUGUST, 2));
        cashAccountTransaction2.setCreationDate(LocalDateTime.of(2020, Month.AUGUST, 2, 10, 10, 0));
        session().saveOrUpdate(cashAccountTransaction2);

        var cashAccountTransaction3 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction3.setValutaDate(LocalDate.of(2020, Month.AUGUST, 2));
        cashAccountTransaction2.setCreationDate(LocalDateTime.of(2020, Month.AUGUST, 2, 11, 10, 0));
        session().saveOrUpdate(cashAccountTransaction3);

        var cashAccountTransaction4 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction4.setValutaDate(LocalDate.of(2020, Month.AUGUST, 3));
        cashAccountTransaction4.setDeletionDate(LocalDateTime.now());
        session().saveOrUpdate(cashAccountTransaction4);

        graphGenerator.createCashAccountTransaction();

        flush();

        QueryResults<DtoCashAccountTransactionOverview> result = repository.loadOverview(cashAccount, pagingParams);

        Assertions.assertEquals(3, result.getTotal());
        Assertions.assertEquals(2, result.getResults().size());
        Assertions.assertEquals(cashAccountTransaction3.getId(), result.getResults().get(0).getId());
        Assertions.assertEquals(cashAccountTransaction2.getId(), result.getResults().get(1).getId());
    }
}