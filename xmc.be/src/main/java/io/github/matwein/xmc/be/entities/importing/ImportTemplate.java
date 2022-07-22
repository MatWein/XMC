package io.github.matwein.xmc.be.entities.importing;

import io.github.matwein.xmc.be.entities.PersistentObject;
import io.github.matwein.xmc.common.stubs.importing.CsvSeparator;
import io.github.matwein.xmc.common.stubs.importing.ImportTemplateType;
import io.github.matwein.xmc.common.stubs.importing.ImportType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = ImportTemplate.TABLE_NAME, uniqueConstraints = @UniqueConstraint(columnNames = { ImportTemplate.COLUMN_TYPE, ImportTemplate.COLUMN_NAME }))
public class ImportTemplate extends PersistentObject {
	public static final String TABLE_NAME = "IMPORT_TEMPLATES";
	
	static final String COLUMN_NAME = "NAME";
	static final String COLUMN_START_WITH_LINE = "START_WITH_LINE";
	static final String COLUMN_IMPORT_TYPE = "IMPORT_TYPE";
	static final String COLUMN_CSV_SEPARATOR = "CSV_SEPARATOR";
	static final String COLUMN_TYPE = "TYPE";
	static final String COLUMN_ENCODING = "ENCODING";
	
	@Column(name = COLUMN_NAME, nullable = false)
	private String name;
	
	@Column(name = COLUMN_START_WITH_LINE, nullable = false)
	private int startWithLine;
	
	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_IMPORT_TYPE, nullable = false)
	private ImportType importType;
	
	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_CSV_SEPARATOR, nullable = false)
	private CsvSeparator csvSeparator;
	
	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_TYPE, nullable = false)
	private ImportTemplateType type;
	
	@Column(name = COLUMN_ENCODING, nullable = false, length = 25)
	private String encoding;
	
	@OneToMany(mappedBy = "importTemplate")
	private Set<ImportTemplateColumnMapping> columnMappings = new HashSet<>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getStartWithLine() {
		return startWithLine;
	}
	
	public void setStartWithLine(int startWithLine) {
		this.startWithLine = startWithLine;
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
	
	public ImportTemplateType getType() {
		return type;
	}
	
	public void setType(ImportTemplateType type) {
		this.type = type;
	}
	
	public Set<ImportTemplateColumnMapping> getColumnMappings() {
		return columnMappings;
	}
	
	public void setColumnMappings(Set<ImportTemplateColumnMapping> columnMappings) {
		this.columnMappings = columnMappings;
	}
	
	public String getEncoding() {
		return encoding;
	}
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
