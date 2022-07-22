package io.github.matwein.xmc.common.stubs.importing;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DtoImportData<T extends Enum<T>> implements Serializable {
    private File fileToImport;
    private int startWithLine;
    private List<DtoColumnMapping<T>> colmuns = new ArrayList<>();
    private boolean saveTemplate;
    private String templateToSaveName;
    private ImportType importType;
    private CsvSeparator csvSeparator;
    private String encoding;
	
	public File getFileToImport() {
		return fileToImport;
	}
	
	public void setFileToImport(File fileToImport) {
		this.fileToImport = fileToImport;
	}
	
	public int getStartWithLine() {
        return startWithLine;
    }

    public void setStartWithLine(int startWithLine) {
        this.startWithLine = startWithLine;
    }

    public List<DtoColumnMapping<T>> getColmuns() {
        return colmuns;
    }

    public void setColmuns(List<DtoColumnMapping<T>> colmuns) {
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
	
	public CsvSeparator getCsvSeparator() {
		return csvSeparator;
	}
	
	public void setCsvSeparator(CsvSeparator csvSeparator) {
		this.csvSeparator = csvSeparator;
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	@Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fileToImport", fileToImport)
                .append("startWithLine", startWithLine)
                .append("importType", importType)
                .toString();
    }
}
