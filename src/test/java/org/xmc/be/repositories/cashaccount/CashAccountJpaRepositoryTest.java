package org.xmc.be.repositories.cashaccount;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;

class CashAccountJpaRepositoryTest extends IntegrationTest {
    @Autowired
    private CashAccountJpaRepository repository;

    @Test
    void testFindById() {
        throw new RuntimeException("not yet implemented");
    }
}