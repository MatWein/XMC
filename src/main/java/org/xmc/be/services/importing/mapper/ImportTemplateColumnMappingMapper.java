package org.xmc.be.services.importing.mapper;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.importing.ImportTemplate;
import org.xmc.be.entities.importing.ImportTemplateColumnMapping;
import org.xmc.common.stubs.importing.DtoColumnMapping;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImportTemplateColumnMappingMapper {
	public <T extends Enum<T>> List<ImportTemplateColumnMapping> mapAll(
			ImportTemplate importTemplate,
			List<DtoColumnMapping<T>> columns) {
		
		return columns.stream()
				.filter(column -> column.getField() != null)
				.map(columnToMap -> map(importTemplate, columnToMap))
				.collect(Collectors.toList());
	}
	
	private <T extends Enum<T>> ImportTemplateColumnMapping map(ImportTemplate importTemplate, DtoColumnMapping<T> columnToMap) {
		var mappedMapping = new ImportTemplateColumnMapping();
		
		mappedMapping.setImportTemplate(importTemplate);
		mappedMapping.setColumnType(columnToMap.getField().name());
		mappedMapping.setColumnIndex(columnToMap.getColumn());
		
		return mappedMapping;
	}
}
