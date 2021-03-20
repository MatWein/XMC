package org.xmc.fe.ui.components.treetable;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeTableView;
import org.xmc.fe.FeConstants;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

public class BaseTreeTable<T> extends TreeTableView<T> {
	private Runnable onTreeItemSelectionChanged;
	private PauseTransition pause = new PauseTransition(FeConstants.DEFAULT_DELAY);
	
	public BaseTreeTable() {
		setPlaceholder(new Label(MessageAdapter.getByKey(MessageKey.TABLE_NO_CONTENT)));
	}
	
	public SelectionMode getSelectionMode() {
		return getSelectionModel().getSelectionMode();
	}
	
	public void setSelectionMode(SelectionMode selectionMode) {
		getSelectionModel().setSelectionMode(selectionMode);
	}
	
	void onTreeItemSelectionChanged() {
		if (onTreeItemSelectionChanged != null) {
			pause.setOnFinished(e -> onTreeItemSelectionChanged.run());
			pause.playFromStart();
		}
	}
	
	public Runnable getOnTreeItemSelectionChanged() {
		return onTreeItemSelectionChanged;
	}
	
	public void setOnTreeItemSelectionChanged(Runnable onTreeItemSelectionChanged) {
		this.onTreeItemSelectionChanged = onTreeItemSelectionChanged;
	}
}
