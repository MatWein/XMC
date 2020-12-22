package org.xmc.be.services.importing.mapper;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.importing.ImportTemplate;
import org.xmc.be.entities.importing.ImportTemplateColumnMapping;
import org.xmc.common.stubs.importing.DtoColumnMapping;
import org.xmc.common.stubs.importing.DtoImportTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ImportTemplateToDtoImportTemplateMapper {
	public <T extends Enum<T>> List<DtoImportTemplate<T>> mapAll(List<ImportTemplate> templates, Class<T> columnType) {
		return templates.stream()
				.map(template -> map(template, columnType))
				.collect(Collectors.toList());
	}
	
	public <T extends Enum<T>> DtoImportTemplate<T> map(ImportTemplate template, Class<T> columnType) {
		DtoImportTemplate<T> dto = new DtoImportTemplate<>();
		
		dto.setCsvSeparator(template.getCsvSeparator());
		dto.setImportType(template.getImportType());
		dto.setStartWithLine(template.getStartWithLine());
		dto.setTemplateToSaveName(template.getName());
		dto.setColmuns(mapColumns(template.getColumnMappings(), columnType));
		dto.setEncoding(template.getEncoding());
		
		return dto;
	}
	
	private <T extends Enum<T>> List<DtoColumnMapping<T>> mapColumns(Set<ImportTemplateColumnMapping> columnMappings, Class<T> columnType) {
		return columnMappings.stream()
				.map(column -> mapColumn(column, columnType))
				.collect(Collectors.toList());
	}
	
	private <T extends Enum<T>> DtoColumnMapping<T> mapColumn(ImportTemplateColumnMapping column, Class<T> columnType) {
		DtoColumnMapping<T> dto = new DtoColumnMapping<>();
		
		dto.setColumn(column.getColumnIndex());
		dto.setField(Enum.valueOf(columnType, column.getColumnType()));
		
		return dto;
	}
}
