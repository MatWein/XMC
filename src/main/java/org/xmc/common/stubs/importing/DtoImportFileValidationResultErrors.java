package org.xmc.common.stubs.importing;

import java.io.Serializable;

public class DtoImportFileValidationResultErrors implements Serializable {
    private int line;
    private String description;

    public DtoImportFileValidationResultErrors() {
    }

    public DtoImportFileValidationResultErrors(int line, String description) {
        this.line = line;
        this.description = description;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
