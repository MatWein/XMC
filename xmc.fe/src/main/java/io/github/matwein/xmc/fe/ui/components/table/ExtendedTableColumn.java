package io.github.matwein.xmc.fe.ui.components.table;

import javafx.scene.control.TableColumn;

public class ExtendedTableColumn<S, T> extends TableColumn<S, T> {
    private String sortField;
    private boolean avoidAutoResize = false;
    private boolean showSum = false;

    public ExtendedTableColumn() {
        this.setVisible(false);
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean isAvoidAutoResize() {
        return avoidAutoResize;
    }

    public void setAvoidAutoResize(boolean avoidAutoResize) {
        this.avoidAutoResize = avoidAutoResize;
    }
	
	public boolean isShowSum() {
		return showSum;
	}
	
	public void setShowSum(boolean showSum) {
		this.showSum = showSum;
	}
}
