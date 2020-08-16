package org.xmc.be.repositories.cashaccount;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;

class CashAccountTransactionRepositoryTest extends IntegrationTest {
    @Autowired
    private CashAccountTransactionRepository repository;

    @Test
    void testLoadOverview() {
        throw new RuntimeException("not yet implemented");
    }
}