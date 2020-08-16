package org.xmc.be.repositories.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.JUnitTestBase;
import org.xmc.be.IntegrationTest;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest extends IntegrationTest {
    @Autowired
    private CategoryRepository repository;

    @Test
    void testLoadOverview() {
        throw new RuntimeException("not yet implemented");
    }
}