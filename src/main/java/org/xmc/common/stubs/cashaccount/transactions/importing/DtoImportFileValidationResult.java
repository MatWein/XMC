package org.xmc.common.stubs.cashaccount.transactions.importing;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class DtoImportFileValidationResult implements Serializable {
    private List<DtoImportFileValidationResultLine> firstLineFields = Lists.newArrayList();
    private List<DtoImportFileValidationResultErrors> errors = Lists.newArrayList();

    private int validTransactionCount;
    private int invalidTransactionCount;

    public List<DtoImportFileValidationResultLine> getFirstLineFields() {
        return firstLineFields;
    }

    public void setFirstLineFields(List<DtoImportFileValidationResultLine> firstLineFields) {
        this.firstLineFields = firstLineFields;
    }

    public List<DtoImportFileValidationResultErrors> getErrors() {
        return errors;
    }

    public void setErrors(List<DtoImportFileValidationResultErrors> errors) {
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
