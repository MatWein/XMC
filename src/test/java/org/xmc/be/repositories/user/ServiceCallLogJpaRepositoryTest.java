package org.xmc.be.repositories.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;

class ServiceCallLogJpaRepositoryTest extends IntegrationTest {
    @Autowired
    private ServiceCallLogJpaRepository repository;

    @Test
    void testFindById() {
        throw new RuntimeException("not yet implemented");
    }
}