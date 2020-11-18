package org.xmc.common.stubs.cashaccount.transactions.importing;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.xmc.common.stubs.importing.DtoColumnMapping;

import java.io.Serializable;
import java.util.List;

public class DtoCashAccountTransactionImportData implements Serializable {
    private String filePath;
    private int startWithLine;
    private List<DtoColumnMapping<CashAccountTransactionImportColmn>> colmuns = Lists.newArrayList();
    private boolean saveTemplate;
    private String templateToSaveName;
    private ImportType importType;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getStartWithLine() {
        return startWithLine;
    }

    public void setStartWithLine(int startWithLine) {
        this.startWithLine = startWithLine;
    }

    public List<DtoColumnMapping<CashAccountTransactionImportColmn>> getColmuns() {
        return colmuns;
    }

    public void setColmuns(List<DtoColumnMapping<CashAccountTransactionImportColmn>> colmuns) {
        this.colmuns = colmuns;
    }

    public boolean isSaveTemplate() {
        return saveTemplate;
    }

    public void setSaveTemplate(boolean saveTemplate) {
        this.saveTemplate = saveTemplate;
    }

    public String getTemplateToSaveName() {
        return templateToSaveName;
    }

    public void setTemplateToSaveName(String templateToSaveName) {
        this.templateToSaveName = templateToSaveName;
    }

    public ImportType getImportType() {
        return importType;
    }

    public void setImportType(ImportType importType) {
        this.importType = importType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("filePath", filePath)
                .append("startWithLine", startWithLine)
                .append("importType", importType)
                .toString();
    }
}
