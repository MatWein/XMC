package io.github.matwein.xmc.be.common.factories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.github.matwein.xmc.JUnitTestBase;
import io.github.matwein.xmc.be.entities.BinaryData;

class BinaryDataFactoryTest extends JUnitTestBase {
    private BinaryDataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new BinaryDataFactory();
    }

    @Test
    void testCreate() {
        byte[] content = "test".getBytes();
        String description = "description";

        BinaryData result = factory.create(content, description);

        Assertions.assertSame(description, result.getDescription());
        Assertions.assertSame(content, result.getRawData());
        Assertions.assertEquals("098f6bcd4621d373cade4e832627b4f6", result.getHash());
        Assertions.assertEquals(4L, result.getSize());
        Assertions.assertNull(result.getId());
    }
}