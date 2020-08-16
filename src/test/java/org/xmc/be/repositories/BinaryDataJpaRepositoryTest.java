package org.xmc.be.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;

class BinaryDataJpaRepositoryTest extends IntegrationTest {
    @Autowired
    private BinaryDataJpaRepository repository;

    @Test
    void testFindById() {
        throw new RuntimeException("not yet implemented");
    }
}