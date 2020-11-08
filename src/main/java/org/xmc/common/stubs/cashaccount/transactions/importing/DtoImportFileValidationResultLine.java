package org.xmc.common.stubs.cashaccount.transactions.importing;

import java.io.Serializable;

public class DtoImportFileValidationResultLine<T extends Enum<T>> implements Serializable {
    private T column;
    private Object value;

    public T getColumn() {
        return column;
    }

    public void setColumn(T column) {
        this.column = column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
