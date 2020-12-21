package org.xmc.be.entities.importing;

import org.xmc.be.entities.PersistentObject;

import javax.persistence.*;

@Entity
@Table(name = ImportTemplateColumnMapping.TABLE_NAME, uniqueConstraints = @UniqueConstraint(columnNames = {
		ImportTemplateColumnMapping.COLUMN_IMPORT_TEMPLATE_ID,
		ImportTemplateColumnMapping.COLUMN_COLUMN_INDEX }))
public class ImportTemplateColumnMapping extends PersistentObject {
	public static final String TABLE_NAME = "IMPORT_TEMPLATE_COLUMN_MAPPINGS";
	
	static final String COLUMN_IMPORT_TEMPLATE_ID = "IMPORT_TEMPLATE_ID";
	static final String COLUMN_COLUMN_INDEX = "COLUMN_INDEX";
	static final String COLUMN_COLUMN_TYPE = "COLUMN_TYPE";
	
	@ManyToOne(optional = false)
	@JoinColumn(name = COLUMN_IMPORT_TEMPLATE_ID)
	private ImportTemplate importTemplate;
	
	@Column(name = COLUMN_COLUMN_INDEX, nullable = false)
	private int columnIndex;
	
	@Column(name = COLUMN_COLUMN_TYPE, nullable = false)
	private String columnType;
	
	public ImportTemplate getImportTemplate() {
		return importTemplate;
	}
	
	public void setImportTemplate(ImportTemplate importTemplate) {
		this.importTemplate = importTemplate;
	}
	
	public int getColumnIndex() {
		return columnIndex;
	}
	
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	
	public String getColumnType() {
		return columnType;
	}
	
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
}
