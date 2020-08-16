package org.xmc.be.repositories.bank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;

class BankJpaRepositoryTest extends IntegrationTest {
    @Autowired
    private BankJpaRepository repository;

    @Test
    void testFindByDeletionDateIsNull() {
        throw new RuntimeException("not yet implemented");
    }
}