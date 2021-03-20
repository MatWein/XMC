package org.xmc.fe.ui.components.treetable;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.util.Callback;

public class TreeTableCellValueFactory implements Callback<CellDataFeatures, ObservableValue> {
	@Override
	public ObservableValue call(CellDataFeatures cellDataFeatures) {
		Object value = cellDataFeatures.getValue().getValue();
		if (value == null) {
			return null;
		}
		
		return new ReadOnlyObjectWrapper<>(value);
	}
}
