package io.github.matwein.xmc.be.services.importing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.be.entities.importing.ImportTemplate;
import io.github.matwein.xmc.be.entities.importing.ImportTemplateColumnMapping;
import io.github.matwein.xmc.be.entities.importing.ImportTemplateType;
import io.github.matwein.xmc.be.repositories.importing.ImportTemplateColumnMappingJpaRepository;
import io.github.matwein.xmc.be.repositories.importing.ImportTemplateJpaRepository;

import java.util.Optional;

@Component
public class ImportTemplateDeleteController {
	private final ImportTemplateJpaRepository importTemplateJpaRepository;
	private final ImportTemplateColumnMappingJpaRepository importTemplateColumnMappingJpaRepository;
	
	@Autowired
	public ImportTemplateDeleteController(
			ImportTemplateJpaRepository importTemplateJpaRepository,
			ImportTemplateColumnMappingJpaRepository importTemplateColumnMappingJpaRepository) {
		
		this.importTemplateJpaRepository = importTemplateJpaRepository;
		this.importTemplateColumnMappingJpaRepository = importTemplateColumnMappingJpaRepository;
	}
	
	public void deleteExistingTemplate(String name, ImportTemplateType templateType) {
		Optional<ImportTemplate> existingTemplate = importTemplateJpaRepository.findByTypeAndName(templateType, name);
		
		if (existingTemplate.isPresent()) {
			deleteExistingTemplate(existingTemplate.get());
		}
	}
	
	public void deleteExistingTemplate(long templateId) {
		Optional<ImportTemplate> importTemplate = importTemplateJpaRepository.findById(templateId);
		
		if (importTemplate.isPresent()) {
			deleteExistingTemplate(importTemplate.get());
		}
	}
	
	public void deleteExistingTemplate(ImportTemplate existingTemplate) {
		for (ImportTemplateColumnMapping columnMapping : existingTemplate.getColumnMappings()) {
			importTemplateColumnMappingJpaRepository.delete(columnMapping);
		}
		
		importTemplateColumnMappingJpaRepository.flush();
		importTemplateJpaRepository.delete(existingTemplate);
		importTemplateJpaRepository.flush();
	}
}
