package org.xmc.fe.ui.validation.components.autocomplete;

import java.util.List;

public interface IAutoCompleteController<T> {
    List<T> search(String searchValue, int limit);
}
