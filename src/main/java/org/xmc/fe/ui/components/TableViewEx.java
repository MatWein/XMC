package org.xmc.fe.ui.components;

import javafx.scene.control.Label;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

public class TableViewEx<T> extends javafx.scene.control.TableView<T> {
    public TableViewEx() {
        this.setPlaceholder(new Label(MessageAdapter.getByKey(MessageKey.TABLE_NO_CONTENT)));
    }
}
