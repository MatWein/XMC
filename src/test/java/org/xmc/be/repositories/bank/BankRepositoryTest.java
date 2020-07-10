package org.xmc.be.repositories.bank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;

class BankRepositoryTest extends IntegrationTest {
    @Autowired
    private BankRepository repository;

    @Test
    void testLoadOverview() {
        throw new RuntimeException("not yet implemented");
    }
}