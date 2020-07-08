package org.xmc.fe.ui.components.table;

import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.apache.commons.beanutils.PropertyUtils;

public class NestedPropertyValueFactory implements Callback<TableColumn.CellDataFeatures, ObservableValue> {
    private final String property;

    public NestedPropertyValueFactory(@NamedArg("property") String property) {
        this.property = property;
    }

    @Override
    public ObservableValue call(TableColumn.CellDataFeatures param) {
        return getCellDataReflectively(param.getValue());
    }

    private ObservableValue getCellDataReflectively(Object rowData) {
        if (getProperty() == null || getProperty().isEmpty() || rowData == null) return null;

        try {
            Object value = PropertyUtils.getNestedProperty(rowData, getProperty());
            Object mappedValue = mapValue(value);
            return new ReadOnlyObjectWrapper<>(mappedValue);
        } catch (Throwable e) {
            throw new RuntimeException(String.format("Could not read property '%s' from: %s", getProperty(), rowData), e);
        }
    }

    protected Object mapValue(Object value) {
        return value;
    }

    public final String getProperty() { return property; }
}
