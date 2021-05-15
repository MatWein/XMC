package io.github.matwein.xmc.fe.common;

import javafx.util.Callback;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReflectionUtilTest {
    @Test
    void testCreateNewInstanceFactory() {
        Callback<Class<?>, Object> newInstanceFactory = XmcFrontendContext.createNewInstanceFactory();

        Object result = newInstanceFactory.call(ReflectionUtilTest.class);
        Assertions.assertNotNull(result);
    }

    @Test
    void testCreateNewInstance() {
	    ReflectionUtilTest result = ReflectionUtil.createNewInstance(ReflectionUtilTest.class);
        Assertions.assertNotNull(result);
    }

    @Test
    void testForName() {
        Class<ReflectionUtilTest> expectedResult = ReflectionUtilTest.class;
        Class<?> result = ReflectionUtil.forName(expectedResult.getName());
        Assertions.assertEquals(expectedResult, result);
    }
}