package org.xmc.common.utils;

import javafx.util.Callback;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;

class ReflectionUtilTest extends JUnitTestBase {
    @Test
    void testCreateNewInstanceFactory() {
        Callback<Class<?>, Object> newInstanceFactory = ReflectionUtil.createNewInstanceFactory();

        Object result = newInstanceFactory.call(ReflectionUtilTest.class);
        Assert.assertNotNull(result);
    }

    @Test
    void testCreateNewInstance() {
        CrypterTest result = ReflectionUtil.createNewInstance(CrypterTest.class);
        Assert.assertNotNull(result);
    }

    @Test
    void testForName() {
        Class<ReflectionUtilTest> expectedResult = ReflectionUtilTest.class;
        Class<?> result = ReflectionUtil.forName(expectedResult.getName());
        Assert.assertEquals(expectedResult, result);
    }
}