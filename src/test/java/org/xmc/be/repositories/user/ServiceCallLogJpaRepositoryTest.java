package org.xmc.be.repositories.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.be.entities.user.ServiceCallLog;

import java.util.Optional;

class ServiceCallLogJpaRepositoryTest extends IntegrationTest {
    @Autowired
    private ServiceCallLogJpaRepository repository;

    @Test
    void testFindById() {
        graphGenerator.createServiceCallLog();
        ServiceCallLog expectedResult = graphGenerator.createServiceCallLog();
        graphGenerator.createServiceCallLog();

        flush();

        Optional<ServiceCallLog> result = repository.findById(expectedResult.getId());

        Assertions.assertEquals(expectedResult, result.get());
    }
}