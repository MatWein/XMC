package org.xmc.common.stubs.importing;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class DtoImportFileValidationResult<T extends Serializable> implements Serializable {
    private List<T> successfullyReadLines = Lists.newArrayList();
    private List<DtoImportFileValidationResultError> errors = Lists.newArrayList();

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
