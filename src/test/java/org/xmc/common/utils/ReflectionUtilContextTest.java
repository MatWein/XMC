package org.xmc.common.utils;

import javafx.util.Callback;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.xmc.be.IntegrationTest;
import org.xmc.be.services.login.UserLoginService;

class ReflectionUtilContextTest extends IntegrationTest {
    @Test
    void testCreateNewInstanceFactory() {
        Callback<Class<?>, Object> newInstanceFactory = ReflectionUtil.createNewInstanceFactory();

        Object result = newInstanceFactory.call(UserLoginService.class);
        Assert.assertNotNull(result);
    }
}
