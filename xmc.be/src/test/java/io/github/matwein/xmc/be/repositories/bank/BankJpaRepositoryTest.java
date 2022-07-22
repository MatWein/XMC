package io.github.matwein.xmc.be.repositories.bank;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.Bank;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        Assertions.assertEquals(Set.of(bank1, bank2), new HashSet<>(result));
    }
}