package io.github.matwein.xmc.be.repositories.cashaccount;

import com.google.common.collect.Sets;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class CashAccountJpaRepositoryTest extends IntegrationTest {
    @Autowired
    private CashAccountJpaRepository repository;

    @Test
    void testFindById() {
        graphGenerator.createCashAccount();
        CashAccount expectedResult = graphGenerator.createCashAccount();
        graphGenerator.createCashAccount();

        flushAndClear();

        Optional<CashAccount> result = repository.findById(expectedResult.getId());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(expectedResult, result.get());
    }
	
	@Test
	void testFindByDeletionDateIsNull() {
		CashAccount cashAccount1 = graphGenerator.createCashAccount();
		
		CashAccount cashAccount2 = graphGenerator.createCashAccount();
		cashAccount2.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(cashAccount2);
		
		CashAccount cashAccount3 = graphGenerator.createCashAccount();
		
		flushAndClear();
  
		List<CashAccount> result = repository.findByDeletionDateIsNull();
		
		Assertions.assertEquals(Sets.newHashSet(cashAccount1, cashAccount3), Sets.newHashSet(result));
	}
}