package io.github.matwein.xmc.common.utils;

import javafx.util.Callback;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.services.login.UserLoginService;

class ReflectionUtilContextTest extends IntegrationTest {
    @Test
    void testCreateNewInstanceFactory() {
        Callback<Class<?>, Object> newInstanceFactory = ReflectionUtil.createNewInstanceFactory();

        Object result = newInstanceFactory.call(UserLoginService.class);
        Assertions.assertNotNull(result);
    }
}
