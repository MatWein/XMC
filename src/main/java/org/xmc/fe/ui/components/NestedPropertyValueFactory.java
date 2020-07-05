package org.xmc.fe.ui.components;

import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.apache.commons.beanutils.PropertyUtils;

public class NestedPropertyValueFactory<S,T> implements Callback<TableColumn.CellDataFeatures<S,T>, ObservableValue<T>> {
    private final String property;

    public NestedPropertyValueFactory(@NamedArg("property") String property) {
        this.property = property;
    }

    @Override
    public ObservableValue<T> call(TableColumn.CellDataFeatures<S, T> param) {
        return getCellDataReflectively(param.getValue());
    }

    private ObservableValue<T> getCellDataReflectively(S rowData) {
        if (getProperty() == null || getProperty().isEmpty() || rowData == null) return null;

        try {
            T value = (T)PropertyUtils.getNestedProperty(rowData, getProperty());
            return new ReadOnlyObjectWrapper<>(value);
        } catch (Throwable e) {
            throw new RuntimeException(String.format("Could not read property '%s' from: %s", getProperty(), rowData), e);
        }
    }

    public final String getProperty() { return property; }
}
