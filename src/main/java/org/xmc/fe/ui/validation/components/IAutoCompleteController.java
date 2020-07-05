package org.xmc.fe.ui.validation.components;

import java.util.List;

public interface IAutoCompleteController<T> {
    List<T> search(String searchValue, int limit);
}
