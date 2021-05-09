package io.github.matwein.xmc.be.repositories.bank;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.Bank;

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

        Assertions.assertEquals(Sets.newHashSet(bank1, bank2), Sets.newHashSet(result));
    }
}