package org.xmc.be.common.factories;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;
import org.xmc.be.entities.BinaryData;

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

        Assert.assertSame(description, result.getDescription());
        Assert.assertSame(content, result.getRawData());
        Assert.assertEquals("098f6bcd4621d373cade4e832627b4f6", result.getHash());
        Assert.assertEquals(4L, result.getSize());
        Assert.assertNull(result.getId());
    }
}