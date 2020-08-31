package org.xmc.fe.importing;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import org.xmc.common.stubs.importing.DtoColumnMapping;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;

public class ImportColumnFieldCellValueFactory<T extends Enum<T>> implements Callback<CellDataFeatures, ObservableValue> {
    private Class<T> fieldType;

    @Override
    public ObservableValue call(CellDataFeatures param) {
        DtoColumnMapping<T> value = (DtoColumnMapping<T>)param.getValue();
        if (value == null) {
            return null;
        }

        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(fieldType.getEnumConstants());
        comboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(MessageKey.CASHACCOUNT_TRANSACTION_IMPORT_DIALOG_STEP4_COLUMN_PREFIX, t)));
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.getSelectionModel().select(value.getField());
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> value.setField(newValue));
        return new ReadOnlyObjectWrapper<>(comboBox);
    }

    public String getFieldType() {
        return fieldType.getName();
    }

    public void setFieldType(String fieldType) throws ClassNotFoundException {
        this.fieldType = (Class)Class.forName(fieldType);
    }
}
