package org.xmc.common.stubs.importing;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class DtoImportTemplate<T extends Enum<T>> implements Serializable {
	private int startWithLine;
	private List<DtoColumnMapping<T>> colmuns = Lists.newArrayList();
	private String templateToSaveName;
	private ImportType importType;
	private CsvSeparator csvSeparator;
	private String encoding;
	
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
}
