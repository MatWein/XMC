package org.xmc.fe.ui.components.table;

import javafx.scene.control.Label;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

public class TableView<T> extends javafx.scene.control.TableView<T> {
    public TableView() {
        setPlaceholder(new Label(MessageAdapter.getByKey(MessageKey.TABLE_NO_CONTENT)));
    }
}
