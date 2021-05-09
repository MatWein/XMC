package io.github.matwein.xmc.common.stubs.importing;

import java.io.Serializable;

public class DtoImportFileValidationResultError implements Serializable {
    private int line;
    private String description;

    public DtoImportFileValidationResultError() {
    }

    public DtoImportFileValidationResultError(int line, String description) {
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
