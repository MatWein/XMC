package org.xmc.be.common.importing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.importing.ImportTemplate;
import org.xmc.be.entities.importing.ImportTemplateType;
import org.xmc.be.repositories.importing.ImportTemplateJpaRepository;
import org.xmc.common.stubs.importing.CsvSeparator;
import org.xmc.common.stubs.importing.DtoColumnMapping;
import org.xmc.common.stubs.importing.ImportType;

import java.util.List;
import java.util.Optional;

@Component
public class ImportTemplateSaveOrUpdateController {
	private final ImportTemplateJpaRepository importTemplateJpaRepository;
	
	@Autowired
	public ImportTemplateSaveOrUpdateController(ImportTemplateJpaRepository importTemplateJpaRepository) {
		this.importTemplateJpaRepository = importTemplateJpaRepository;
	}
	
	public <T extends Enum<T>> void saveTemplate(
			String name,
			ImportTemplateType templateType,
			ImportType importType,
			CsvSeparator csvSeparator,
			int startWithLine,
			List<DtoColumnMapping<T>> colmuns) {
		
		Optional<ImportTemplate> existingTemplate = importTemplateJpaRepository.findByTypeAndName(templateType, name);
		
	}
}
