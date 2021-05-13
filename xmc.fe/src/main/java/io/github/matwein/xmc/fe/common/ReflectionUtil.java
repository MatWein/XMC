package io.github.matwein.xmc.fe.common;

public class ReflectionUtil {
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
