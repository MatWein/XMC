package org.xmc.fe.ui.components.table;

import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

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
