package io.github.matwein.xmc.common.utils;

import javafx.util.Callback;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import io.github.matwein.xmc.JUnitTestBase;

class ReflectionUtilTest extends JUnitTestBase {
    @Test
    void testCreateNewInstanceFactory() {
        Callback<Class<?>, Object> newInstanceFactory = ReflectionUtil.createNewInstanceFactory();

        Object result = newInstanceFactory.call(ReflectionUtilTest.class);
        Assertions.assertNotNull(result);
    }

    @Test
    void testCreateNewInstance() {
        CrypterTest result = ReflectionUtil.createNewInstance(CrypterTest.class);
        Assertions.assertNotNull(result);
    }

    @Test
    void testForName() {
        Class<ReflectionUtilTest> expectedResult = ReflectionUtilTest.class;
        Class<?> result = ReflectionUtil.forName(expectedResult.getName());
        Assertions.assertEquals(expectedResult, result);
    }
}