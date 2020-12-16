package org.xmc.be.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.be.entities.BinaryData;

import java.util.Optional;

class BinaryDataJpaRepositoryTest extends IntegrationTest {
    @Autowired
    private BinaryDataJpaRepository repository;

    @Test
    void testFindById() {
        graphGenerator.createBinaryData();
        BinaryData expectedResult = graphGenerator.createBinaryData();
        graphGenerator.createBinaryData();

        flush();

        Optional<BinaryData> result = repository.findById(expectedResult.getId());

        Assertions.assertEquals(expectedResult, result.get());
    }
}