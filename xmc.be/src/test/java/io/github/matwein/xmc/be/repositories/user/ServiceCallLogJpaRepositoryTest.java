package io.github.matwein.xmc.be.repositories.user;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.user.ServiceCallLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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