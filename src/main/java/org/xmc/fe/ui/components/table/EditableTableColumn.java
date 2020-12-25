package org.xmc.fe.ui.components.table;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;

public class EditableTableColumn extends TableColumn<Object, String> {
	public EditableTableColumn() {
		this.setCellFactory(TextFieldTableCell.forTableColumn());
		this.setEditable(true);
	}
}
