package io.github.matwein.xmc.common.utils;

import javafx.util.Callback;
import io.github.matwein.xmc.Main;

public class ReflectionUtil {
    public static Callback<Class<?>, Object> createNewInstanceFactory() {
        return type -> {
            if (Main.applicationContext == null) {
                return createNewInstance(type);
            } else {
                return Main.applicationContext.getBean(type);
            }
        };
    }

    public static <T> T createNewInstance(Class<T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Throwable e) {
            String message = String.format("Error on creating instance of '%s'.", type.getName());
            throw new RuntimeException(message, e);
        }
    }

    public static Class<?> forName(String type) {
        try {
            return Class.forName(type);
        } catch (Throwable e) {
            String message = String.format("Error on creating class for name '%s'.", type);
            throw new RuntimeException(message, e);
        }
    }
}
