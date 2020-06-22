package org.xmc.fe.ui.converter;

import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class GenericItemToStringConverter<T> extends StringConverter<T> {
    private final Function<T, String> toStringFunction;

    public static <T> GenericItemToStringConverter<T> getInstance(Function<T, String> toStringFunction) {
        return new GenericItemToStringConverter<>(toStringFunction);
    }

    private Map<String, T> cache = new HashMap<>();

    private GenericItemToStringConverter(Function<T, String> toStringFunction) {
        this.toStringFunction = toStringFunction;
    }

    @Override
    public String toString(T object) {
        if (object == null) {
            return null;
        }

        String stringRepresentation = toStringFunction.apply(object);
        cache.put(stringRepresentation, object);
        return stringRepresentation;
    }

    @Override
    public T fromString(String stringRepresentation) {
        return cache.get(stringRepresentation);
    }
}
