package io.github.matwein.xmc.fe.importing;

import io.github.matwein.xmc.common.stubs.importing.DtoColumnMapping;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.converter.GenericItemToStringConverter;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class DtoColumnMappingComboBoxCellValueFactory<T extends Enum<T>> implements Callback<CellDataFeatures, ObservableValue> {
    private Class<T> fieldType;
    private MessageKey translationKey;

    @Override
    public ObservableValue call(CellDataFeatures param) {
        DtoColumnMapping<T> value = (DtoColumnMapping<T>)param.getValue();
        if (value == null) {
            return null;
        }

        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.getItems().add(null);
        comboBox.getItems().addAll(fieldType.getEnumConstants());
        comboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(translationKey, t)));
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
	
	public String getTranslationKey() {
		return translationKey.name();
	}
	
	public void setTranslationKey(String translationKey) {
		this.translationKey = MessageKey.valueOf(translationKey);
	}
}
