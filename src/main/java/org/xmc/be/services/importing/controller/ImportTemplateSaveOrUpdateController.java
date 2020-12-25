package org.xmc.be.services.importing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.importing.ImportTemplate;
import org.xmc.be.entities.importing.ImportTemplateColumnMapping;
import org.xmc.be.entities.importing.ImportTemplateType;
import org.xmc.be.repositories.importing.ImportTemplateColumnMappingJpaRepository;
import org.xmc.be.repositories.importing.ImportTemplateJpaRepository;
import org.xmc.be.services.importing.mapper.ImportTemplateColumnMappingMapper;
import org.xmc.common.stubs.importing.CsvSeparator;
import org.xmc.common.stubs.importing.DtoColumnMapping;
import org.xmc.common.stubs.importing.ImportType;

import java.util.List;

@Component
public class ImportTemplateSaveOrUpdateController {
	private final ImportTemplateJpaRepository importTemplateJpaRepository;
	private final ImportTemplateColumnMappingJpaRepository importTemplateColumnMappingJpaRepository;
	private final ImportTemplateColumnMappingMapper importTemplateColumnMappingMapper;
	private final ImportTemplateDeleteController importTemplateDeleteController;
	
	@Autowired
	public ImportTemplateSaveOrUpdateController(
			ImportTemplateJpaRepository importTemplateJpaRepository,
			ImportTemplateColumnMappingJpaRepository importTemplateColumnMappingJpaRepository,
			ImportTemplateColumnMappingMapper importTemplateColumnMappingMapper,
			ImportTemplateDeleteController importTemplateDeleteController) {
		
		this.importTemplateJpaRepository = importTemplateJpaRepository;
		this.importTemplateColumnMappingJpaRepository = importTemplateColumnMappingJpaRepository;
		this.importTemplateColumnMappingMapper = importTemplateColumnMappingMapper;
		this.importTemplateDeleteController = importTemplateDeleteController;
	}
	
	public <T extends Enum<T>> void saveTemplate(
			String name,
			ImportTemplateType templateType,
			ImportType importType,
			CsvSeparator csvSeparator,
			int startWithLine,
			List<DtoColumnMapping<T>> colmuns,
			String encoding) {
		
		importTemplateDeleteController.deleteExistingTemplate(name, templateType);
		
		saveNewTemplate(name, templateType, importType, csvSeparator, startWithLine, colmuns, encoding);
	}
	
	private <T extends Enum<T>> void saveNewTemplate(
			String name,
			ImportTemplateType templateType,
			ImportType importType,
			CsvSeparator csvSeparator,
			int startWithLine,
			List<DtoColumnMapping<T>> colmuns,
			String encoding) {
		
		ImportTemplate importTemplate = new ImportTemplate();
		
		importTemplate.setType(templateType);
		importTemplate.setStartWithLine(startWithLine);
		importTemplate.setName(name);
		importTemplate.setImportType(importType);
		importTemplate.setCsvSeparator(csvSeparator);
		importTemplate.setEncoding(encoding);
		
		importTemplateJpaRepository.save(importTemplate);
		
		List<ImportTemplateColumnMapping> mappedColumnMappings = importTemplateColumnMappingMapper.mapAll(importTemplate, colmuns);
		
		for (ImportTemplateColumnMapping columnMapping : mappedColumnMappings) {
			importTemplateColumnMappingJpaRepository.save(columnMapping);
		}
		
		importTemplate.getColumnMappings().addAll(mappedColumnMappings);
	}
}
