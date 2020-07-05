package org.xmc.fe.ui.validation.components.autocomplete;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AutoCompleteByConverterController<T> implements IAutoCompleteController<T> {
    private final List<T> items;
    private final Function<T, String> converter;

    public AutoCompleteByConverterController(
            List<T> items,
            Function<T, String> converter) {

        this.items = items;
        this.converter = converter;
    }

    @Override
    public List<T> search(String searchValue, int limit) {
        return items.stream()
                .filter(dto -> matches(searchValue, dto))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private boolean matches(String searchValue, T dto) {
        String text = converter.apply(dto);
        return StringUtils.containsIgnoreCase(text, searchValue);
    }
}
