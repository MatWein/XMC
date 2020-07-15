package org.xmc.fe.ui.components.table;

import javafx.scene.control.TableColumn;

public class TableColumnEx<S, T> extends TableColumn<S, T> {
    private String sortField;
    private boolean avoidAutoResize = false;

    public TableColumnEx() {
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
}
