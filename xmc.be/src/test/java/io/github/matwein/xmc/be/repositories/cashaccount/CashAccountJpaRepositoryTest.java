package io.github.matwein.xmc.be.repositories.cashaccount;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

class CashAccountJpaRepositoryTest extends IntegrationTest {
    @Autowired
    private CashAccountJpaRepository repository;

    @Test
    void testFindById() {
        graphGenerator.createCashAccount();
        CashAccount expectedResult = graphGenerator.createCashAccount();
        graphGenerator.createCashAccount();

        flush();

        Optional<CashAccount> result = repository.findById(expectedResult.getId());

        Assertions.assertEquals(expectedResult, result.get());
    }
}