package io.github.matwein.xmc.fe.ui.components.table;

import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;

public class BaseTable<T> extends TableView<T> {
    public BaseTable() {
        setPlaceholder(new Label(MessageAdapter.getByKey(MessageKey.TABLE_NO_CONTENT)));
    }
	
	public SelectionMode getSelectionMode() {
		return getSelectionModel().getSelectionMode();
	}
	
	public void setSelectionMode(SelectionMode selectionMode) {
		getSelectionModel().setSelectionMode(selectionMode);
	}
}
