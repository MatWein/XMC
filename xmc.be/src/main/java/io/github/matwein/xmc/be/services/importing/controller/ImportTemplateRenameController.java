package io.github.matwein.xmc.be.services.importing.controller;

import io.github.matwein.xmc.CommonConstants;
import io.github.matwein.xmc.be.entities.importing.ImportTemplate;
import io.github.matwein.xmc.be.repositories.importing.ImportTemplateJpaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class ImportTemplateRenameController {
	private final ImportTemplateJpaRepository importTemplateJpaRepository;
	
	@Autowired
	public ImportTemplateRenameController(ImportTemplateJpaRepository importTemplateJpaRepository) {
		this.importTemplateJpaRepository = importTemplateJpaRepository;
	}
	
	public boolean rename(long templateId, String newName) {
		if (StringUtils.isBlank(newName)) {
			return false;
		}
		if (newName.length() >= CommonConstants.MAX_TEXT_LENGTH) {
			return false;
		}
		
		Optional<ImportTemplate> importTemplate = importTemplateJpaRepository.findById(templateId);
		if (importTemplate.isPresent()) {
			importTemplate.get().setName(newName);
			importTemplateJpaRepository.save(importTemplate.get());
		}
		
		return true;
	}
}
