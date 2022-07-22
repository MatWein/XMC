package io.github.matwein.xmc.common.stubs.importing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DtoImportFileValidationResult<T extends Serializable> implements Serializable {
    private List<T> successfullyReadLines = new ArrayList<>();
    private List<DtoImportFileValidationResultError> errors = new ArrayList<>();

    private int validTransactionCount;
    private int invalidTransactionCount;
	
	public List<T> getSuccessfullyReadLines() {
		return successfullyReadLines;
	}
	
	public void setSuccessfullyReadLines(List<T> successfullyReadLines) {
		this.successfullyReadLines = successfullyReadLines;
	}
	
	public List<DtoImportFileValidationResultError> getErrors() {
        return errors;
    }

    public void setErrors(List<DtoImportFileValidationResultError> errors) {
        this.errors = errors;
    }

    public int getValidTransactionCount() {
        return validTransactionCount;
    }

    public void setValidTransactionCount(int validTransactionCount) {
        this.validTransactionCount = validTransactionCount;
    }

    public int getInvalidTransactionCount() {
        return invalidTransactionCount;
    }

    public void setInvalidTransactionCount(int invalidTransactionCount) {
        this.invalidTransactionCount = invalidTransactionCount;
    }
}
