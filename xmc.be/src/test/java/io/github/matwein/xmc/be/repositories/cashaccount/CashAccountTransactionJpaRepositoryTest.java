package io.github.matwein.xmc.be.repositories.cashaccount;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.be.entities.cashaccount.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

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

        Assertions.assertEquals(Lists.newArrayList(cashAccountTransaction1, cashAccountTransaction2, cashAccountTransaction3), result);
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

        Assertions.assertEquals(Lists.newArrayList(cashAccountTransaction3, cashAccountTransaction2, cashAccountTransaction1), result);
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

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(cashAccountTransaction3, result.get());
    }
	
	@Test
	void testFindByCashAccountAndDeletionDateIsNull() {
		CashAccount cashAccount = graphGenerator.createCashAccount();
		
		graphGenerator.createCashAccountTransaction();
		CashAccountTransaction expectedResult = graphGenerator.createCashAccountTransaction(cashAccount);
		graphGenerator.createCashAccountTransaction();
		
		flushAndClear();
		
		List<CashAccountTransaction> result = repository.findByCashAccountAndDeletionDateIsNull(cashAccount);
		
		Assertions.assertEquals(Lists.newArrayList(expectedResult), result);
	}
	
	@Test
	void testFindByCashAccountAndDeletionDateIsNull_NotFound() {
		CashAccount cashAccount = graphGenerator.createCashAccount();
		
		graphGenerator.createCashAccountTransaction();
		
		CashAccountTransaction cashAccountTransaction = graphGenerator.createCashAccountTransaction(cashAccount);
		cashAccountTransaction.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(cashAccountTransaction);
		
		graphGenerator.createCashAccountTransaction();
		
		flushAndClear();
		
		List<CashAccountTransaction> result = repository.findByCashAccountAndDeletionDateIsNull(cashAccount);
		
		Assertions.assertEquals(Lists.newArrayList(), result);
	}
	
	@Test
	void testFindFirstTransaction() {
		CashAccount cashAccount = graphGenerator.createCashAccount();
		
		CashAccountTransaction transaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
		transaction1.setValutaDate(LocalDate.of(2020, Month.JANUARY, 1));
		session().saveOrUpdate(transaction1);
		
		CashAccountTransaction transaction2 = graphGenerator.createCashAccountTransaction(cashAccount);
		transaction2.setValutaDate(LocalDate.of(2019, Month.JANUARY, 1));
		session().saveOrUpdate(transaction2);
		
		CashAccountTransaction transaction3 = graphGenerator.createCashAccountTransaction(cashAccount);
		transaction3.setValutaDate(LocalDate.of(2018, Month.JANUARY, 1));
		transaction3.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(transaction3);
		
		flushAndClear();
		
		Optional<CashAccountTransaction> result = repository.findFirstTransaction(Lists.newArrayList(cashAccount.getId()));
		
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(transaction2, result.get());
	}
	
	@Test
	void testFindTransactionsOnOrAfterDateHavingCategory() {
		Category category = graphGenerator.createCategory("Einkauf");
		
		CashAccount cashAccount = graphGenerator.createCashAccount();
		
		graphGenerator.createCashAccountTransaction();
		
		CashAccountTransaction transaction1 = graphGenerator.createCashAccountTransaction(cashAccount, category);
		transaction1.setValutaDate(LocalDate.of(2021, Month.MARCH, 4));
		session().saveOrUpdate(transaction1);
		
		CashAccountTransaction transaction2 = graphGenerator.createCashAccountTransaction(cashAccount, category);
		transaction2.setValutaDate(LocalDate.of(2021, Month.MARCH, 3));
		session().saveOrUpdate(transaction2);
		
		CashAccountTransaction transaction3 = graphGenerator.createCashAccountTransaction(cashAccount, category);
		transaction3.setValutaDate(LocalDate.of(2021, Month.MARCH, 2));
		session().saveOrUpdate(transaction3);
		
		CashAccountTransaction transaction4 = graphGenerator.createCashAccountTransaction(cashAccount, null);
		transaction4.setValutaDate(LocalDate.of(2021, Month.MARCH, 4));
		session().saveOrUpdate(transaction4);
		
		graphGenerator.createCashAccountTransaction();
		
		flushAndClear();
		
		List<CashAccountTransaction> result = repository.findTransactionsOnOrAfterDateHavingCategory(
				cashAccount,
				LocalDate.of(2021, Month.MARCH, 3));
		
		Assertions.assertEquals(Sets.newHashSet(transaction1, transaction2), Sets.newHashSet(result));
	}
	
	@Test
	void testFindTransactionsBeforeOrOnDate() {
		CashAccount cashAccount = graphGenerator.createCashAccount();
		
		graphGenerator.createCashAccountTransaction();
		
		CashAccountTransaction transaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
		transaction1.setValutaDate(LocalDate.of(2021, Month.MARCH, 4));
		session().saveOrUpdate(transaction1);
		
		CashAccountTransaction transaction2 = graphGenerator.createCashAccountTransaction(cashAccount);
		transaction2.setValutaDate(LocalDate.of(2021, Month.MARCH, 3));
		session().saveOrUpdate(transaction2);
		
		CashAccountTransaction transaction3 = graphGenerator.createCashAccountTransaction(cashAccount);
		transaction3.setValutaDate(LocalDate.of(2021, Month.MARCH, 2));
		session().saveOrUpdate(transaction3);
		
		CashAccountTransaction transaction4 = graphGenerator.createCashAccountTransaction(cashAccount);
		transaction4.setValutaDate(LocalDate.of(2021, Month.MARCH, 2));
		transaction4.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(transaction4);
		
		graphGenerator.createCashAccountTransaction();
		
		flushAndClear();
		
		List<CashAccountTransaction> result = repository.findTransactionsBeforeOrOnDate(
				cashAccount,
				LocalDate.of(2021, Month.MARCH, 3),
				LocalDateTime.now(),
				Long.MAX_VALUE,
				PageRequest.of(0, 10));
		
		Assertions.assertEquals(Sets.newHashSet(transaction2, transaction3), Sets.newHashSet(result));
	}
	
	@Test
	void testFindFirstTransactionBeforeOrOnDate() {
		CashAccount cashAccount = graphGenerator.createCashAccount();
		
		graphGenerator.createCashAccountTransaction();
		
		CashAccountTransaction transaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
		transaction1.setValutaDate(LocalDate.of(2021, Month.MARCH, 4));
		session().saveOrUpdate(transaction1);
		
		CashAccountTransaction transaction2 = graphGenerator.createCashAccountTransaction(cashAccount);
		transaction2.setValutaDate(LocalDate.of(2021, Month.MARCH, 3));
		session().saveOrUpdate(transaction2);
		
		CashAccountTransaction transaction3 = graphGenerator.createCashAccountTransaction(cashAccount);
		transaction3.setValutaDate(LocalDate.of(2021, Month.MARCH, 2));
		session().saveOrUpdate(transaction3);
		
		CashAccountTransaction transaction4 = graphGenerator.createCashAccountTransaction(cashAccount);
		transaction4.setValutaDate(LocalDate.of(2021, Month.MARCH, 3));
		transaction4.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(transaction4);
		
		graphGenerator.createCashAccountTransaction();
		
		flushAndClear();
		
		Optional<CashAccountTransaction> result = repository.findFirstTransactionBeforeOrOnDate(
				cashAccount,
				LocalDate.of(2021, Month.MARCH, 3),
				LocalDateTime.now(),
				Long.MAX_VALUE);
		
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(transaction2, result.get());
	}
	
	@Test
	void testFindMostRecentTransactions() {
		graphGenerator.createCashAccount();
		CashAccount cashAccount1 = graphGenerator.createCashAccount();
		CashAccount cashAccount2 = graphGenerator.createCashAccount();
		graphGenerator.createCashAccount();
		
		CashAccountTransaction transaction1 = graphGenerator.createCashAccountTransaction(cashAccount1);
		transaction1.setValutaDate(LocalDate.of(2021, Month.MARCH, 4));
		session().saveOrUpdate(transaction1);
		
		CashAccountTransaction transaction2 = graphGenerator.createCashAccountTransaction(cashAccount1);
		transaction2.setValutaDate(LocalDate.of(2021, Month.MARCH, 3));
		session().saveOrUpdate(transaction2);
		
		CashAccountTransaction transaction3 = graphGenerator.createCashAccountTransaction(cashAccount2);
		transaction3.setValutaDate(LocalDate.of(2021, Month.MARCH, 2));
		session().saveOrUpdate(transaction3);
		
		CashAccountTransaction transaction4 = graphGenerator.createCashAccountTransaction(cashAccount2);
		transaction4.setValutaDate(LocalDate.of(2021, Month.MARCH, 3));
		transaction4.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(transaction4);
		
		flushAndClear();
		
		List<CashAccountTransaction> result = repository.findMostRecentTransactions(
				Sets.newHashSet(cashAccount1.getId(), cashAccount2.getId()),
				PageRequest.of(0, 10));
		
		Assertions.assertEquals(Lists.newArrayList(transaction1, transaction2, transaction3), result);
	}
	
	@Test
	void testFindAllTransactions() {
		graphGenerator.createCashAccount();
		CashAccount cashAccount1 = graphGenerator.createCashAccount();
		CashAccount cashAccount2 = graphGenerator.createCashAccount();
		graphGenerator.createCashAccount();
		
		CashAccountTransaction transaction1 = graphGenerator.createCashAccountTransaction(cashAccount1);
		transaction1.setValutaDate(LocalDate.of(2021, Month.MARCH, 4));
		session().saveOrUpdate(transaction1);
		
		CashAccountTransaction transaction2 = graphGenerator.createCashAccountTransaction(cashAccount1);
		transaction2.setValutaDate(LocalDate.of(2021, Month.MARCH, 3));
		session().saveOrUpdate(transaction2);
		
		CashAccountTransaction transaction3 = graphGenerator.createCashAccountTransaction(cashAccount2);
		transaction3.setValutaDate(LocalDate.of(2021, Month.MARCH, 2));
		session().saveOrUpdate(transaction3);
		
		CashAccountTransaction transaction4 = graphGenerator.createCashAccountTransaction(cashAccount2);
		transaction4.setValutaDate(LocalDate.of(2021, Month.MARCH, 3));
		transaction4.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(transaction4);
		
		flushAndClear();
		
		List<CashAccountTransaction> result = repository.findAllTransactions(
				Sets.newHashSet(cashAccount1.getId(), cashAccount2.getId()),
				PageRequest.of(0, 10));
		
		Assertions.assertEquals(Lists.newArrayList(transaction3, transaction2, transaction1), result);
	}
}