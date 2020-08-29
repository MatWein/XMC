package org.xmc.be.repositories.cashaccount;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.be.entities.cashaccount.CashAccount;

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

        Assert.assertEquals(expectedResult, result.get());
    }
}