package org.xmc.common.stubs.importing;

import java.io.Serializable;

public class DtoColumnMapping<T extends Enum<T>> implements Serializable {
    private int column;
    private T field;

    public DtoColumnMapping() {
    }

    public DtoColumnMapping(int column, T field) {
        this.column = column;
        this.field = field;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public T getField() {
        return field;
    }

    public void setField(T field) {
        this.field = field;
    }
}
