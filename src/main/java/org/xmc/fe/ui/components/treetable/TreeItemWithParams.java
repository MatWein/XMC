package org.xmc.fe.ui.components.treetable;

import javafx.scene.control.TreeItem;

public class TreeItemWithParams<T, PARAM_TYPE> extends TreeItem<T> {
	private PARAM_TYPE param;
	
	public TreeItemWithParams() {
	}
	
	public TreeItemWithParams(T value) {
		super(value);
	}
	
	public PARAM_TYPE getParam() {
		return param;
	}
	
	public void setParam(PARAM_TYPE param) {
		this.param = param;
	}
}
