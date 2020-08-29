package org.xmc.be.repositories.cashaccount;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

class CashAccountTransactionJpaRepositoryTest extends IntegrationTest {
    @Autowired
    private CashAccountTransactionJpaRepository repository;

    @Test
    void testFindTransactionsAfterDate() {
        CashAccount cashAccount = graphGenerator.createCashAccount();

        graphGenerator.createCashAccountTransaction();
        graphGenerator.createCashAccountTransaction();

        CashAccountTransaction cashAccountTransaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction1.setValutaDate(LocalDate.of(2020, Month.FEBRUARY, 1));
        cashAccountTransaction1.setCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
        session().saveOrUpdate(cashAccountTransaction1);

        CashAccountTransaction cashAccountTransaction2 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction2.setValutaDate(LocalDate.of(2020, Month.FEBRUARY, 1));
        cashAccountTransaction2.setCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
        session().saveOrUpdate(cashAccountTransaction2);

        CashAccountTransaction cashAccountTransaction3 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction3.setValutaDate(LocalDate.of(2020, Month.FEBRUARY, 1));
        cashAccountTransaction3.setCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
        session().saveOrUpdate(cashAccountTransaction3);

        CashAccountTransaction cashAccountTransaction4 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction4.setValutaDate(LocalDate.of(2020, Month.FEBRUARY, 1));
        cashAccountTransaction4.setCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
        cashAccountTransaction4.setDeletionDate(LocalDateTime.now());
        session().saveOrUpdate(cashAccountTransaction4);

        graphGenerator.createCashAccountTransaction();
        graphGenerator.createCashAccountTransaction();

        flush();

        List<CashAccountTransaction> result = repository.findTransactionsAfterDate(cashAccount, LocalDate.of(2020, Month.JANUARY, 1));

        Assert.assertEquals(Lists.newArrayList(cashAccountTransaction1, cashAccountTransaction2, cashAccountTransaction3), result);
    }

    @Test
    void testFindTransactionsBeforeDate() {
        CashAccount cashAccount = graphGenerator.createCashAccount();

        graphGenerator.createCashAccountTransaction();
        graphGenerator.createCashAccountTransaction();

        CashAccountTransaction cashAccountTransaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction1.setValutaDate(LocalDate.of(2020, Month.FEBRUARY, 1));
        cashAccountTransaction1.setCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
        session().saveOrUpdate(cashAccountTransaction1);

        CashAccountTransaction cashAccountTransaction2 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction2.setValutaDate(LocalDate.of(2020, Month.FEBRUARY, 1));
        cashAccountTransaction2.setCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
        session().saveOrUpdate(cashAccountTransaction2);

        CashAccountTransaction cashAccountTransaction3 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction3.setValutaDate(LocalDate.of(2020, Month.FEBRUARY, 1));
        cashAccountTransaction3.setCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
        session().saveOrUpdate(cashAccountTransaction3);

        CashAccountTransaction cashAccountTransaction4 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction4.setValutaDate(LocalDate.of(2020, Month.FEBRUARY, 1));
        cashAccountTransaction4.setCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
        cashAccountTransaction4.setDeletionDate(LocalDateTime.now());
        session().saveOrUpdate(cashAccountTransaction4);

        graphGenerator.createCashAccountTransaction();
        graphGenerator.createCashAccountTransaction();

        flush();

        List<CashAccountTransaction> result = repository.findTransactionsBeforeDate(cashAccount, LocalDate.of(2020, Month.MARCH, 1), null);

        Assert.assertEquals(Lists.newArrayList(cashAccountTransaction3, cashAccountTransaction2, cashAccountTransaction1), result);
    }

    @Test
    void testFindFirstTransactionBeforeDate() {
        CashAccount cashAccount = graphGenerator.createCashAccount();

        graphGenerator.createCashAccountTransaction();
        graphGenerator.createCashAccountTransaction();

        CashAccountTransaction cashAccountTransaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction1.setValutaDate(LocalDate.of(2020, Month.FEBRUARY, 1));
        cashAccountTransaction1.setCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
        session().saveOrUpdate(cashAccountTransaction1);

        CashAccountTransaction cashAccountTransaction2 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction2.setValutaDate(LocalDate.of(2020, Month.FEBRUARY, 1));
        cashAccountTransaction2.setCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
        session().saveOrUpdate(cashAccountTransaction2);

        CashAccountTransaction cashAccountTransaction3 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction3.setValutaDate(LocalDate.of(2020, Month.FEBRUARY, 1));
        cashAccountTransaction3.setCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
        session().saveOrUpdate(cashAccountTransaction3);

        CashAccountTransaction cashAccountTransaction4 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction4.setValutaDate(LocalDate.of(2020, Month.FEBRUARY, 1));
        cashAccountTransaction4.setCreationDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0));
        cashAccountTransaction4.setDeletionDate(LocalDateTime.now());
        session().saveOrUpdate(cashAccountTransaction4);

        graphGenerator.createCashAccountTransaction();
        graphGenerator.createCashAccountTransaction();

        flush();

        Optional<CashAccountTransaction> result = repository.findFirstTransactionBeforeDate(cashAccount, LocalDate.of(2020, Month.MARCH, 1));

        Assert.assertEquals(cashAccountTransaction3, result.get());
    }
}