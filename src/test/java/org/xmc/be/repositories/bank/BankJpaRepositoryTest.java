package org.xmc.be.repositories.bank;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.be.entities.Bank;

import java.time.LocalDateTime;
import java.util.List;

class BankJpaRepositoryTest extends IntegrationTest {
    @Autowired
    private BankJpaRepository repository;

    @Test
    void testFindByDeletionDateIsNull() {
        Bank bank1 = graphGenerator.createBank();

        Bank deletedBank = graphGenerator.createBank();
        deletedBank.setDeletionDate(LocalDateTime.now());
        session().saveOrUpdate(deletedBank);

        Bank bank2 = graphGenerator.createBank();

        flush();

        List<Bank> result = repository.findByDeletionDateIsNull();

        Assert.assertEquals(Sets.newHashSet(bank1, bank2), Sets.newHashSet(result));
    }
}