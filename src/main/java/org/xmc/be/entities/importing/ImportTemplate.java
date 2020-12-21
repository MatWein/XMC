package org.xmc.be.entities.importing;

import com.google.common.collect.Sets;
import org.xmc.be.entities.DeletablePersistentObject;
import org.xmc.common.stubs.importing.CsvSeparator;
import org.xmc.common.stubs.importing.ImportType;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = ImportTemplate.TABLE_NAME, uniqueConstraints = @UniqueConstraint(columnNames = { ImportTemplate.COLUMN_TYPE, ImportTemplate.COLUMN_NAME }))
public class ImportTemplate extends DeletablePersistentObject {
	public static final String TABLE_NAME = "IMPORT_TEMPLATES";
	
	static final String COLUMN_NAME = "NAME";
	static final String COLUMN_START_WITH_LINE = "START_WITH_LINE";
	static final String COLUMN_IMPORT_TYPE = "IMPORT_TYPE";
	static final String COLUMN_CSV_SEPARATOR = "CSV_SEPARATOR";
	static final String COLUMN_TYPE = "TYPE";
	
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
	
	@OneToMany(mappedBy = "importTemplate")
	private Set<ImportTemplateColumnMapping> columnMappings = Sets.newHashSet();
	
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
}
