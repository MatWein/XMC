package org.xmc.be.services.importing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.importing.ImportTemplate;
import org.xmc.be.entities.importing.ImportTemplateType;
import org.xmc.be.repositories.importing.ImportTemplateJpaRepository;
import org.xmc.be.services.importing.controller.ImportTemplateDeleteController;
import org.xmc.be.services.importing.mapper.ImportTemplateToDtoImportTemplateMapper;
import org.xmc.be.services.importing.mapper.ImportTemplateToDtoImportTemplateOverviewMapper;
import org.xmc.common.stubs.importing.DtoImportTemplate;
import org.xmc.common.stubs.importing.DtoImportTemplateOverview;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImportTemplateService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportTemplateService.class);
	
	private final ImportTemplateJpaRepository importTemplateJpaRepository;
	private final ImportTemplateToDtoImportTemplateMapper importTemplateToDtoImportTemplateMapper;
	private final ImportTemplateToDtoImportTemplateOverviewMapper importTemplateToDtoImportTemplateOverviewMapper;
	private final ImportTemplateDeleteController importTemplateDeleteController;
	
	@Autowired
	public ImportTemplateService(
			ImportTemplateJpaRepository importTemplateJpaRepository,
			ImportTemplateToDtoImportTemplateMapper importTemplateToDtoImportTemplateMapper,
			ImportTemplateToDtoImportTemplateOverviewMapper importTemplateToDtoImportTemplateOverviewMapper,
			ImportTemplateDeleteController importTemplateDeleteController) {
		
		this.importTemplateJpaRepository = importTemplateJpaRepository;
		this.importTemplateToDtoImportTemplateMapper = importTemplateToDtoImportTemplateMapper;
		this.importTemplateToDtoImportTemplateOverviewMapper = importTemplateToDtoImportTemplateOverviewMapper;
		this.importTemplateDeleteController = importTemplateDeleteController;
	}
	
	public <T extends Enum<T>> List<DtoImportTemplate<T>> loadImportTemplates(
			AsyncMonitor monitor,
			ImportTemplateType templateType,
			Class<T> columnType) {
		
		LOGGER.info("Loading import templates for enum type '{}'.", columnType.getSimpleName());
		monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_IMPORT_TEMPLATES);
		
		List<ImportTemplate> templates = importTemplateJpaRepository.findByType(templateType);
		return importTemplateToDtoImportTemplateMapper.mapAll(templates, columnType);
	}
	
	public List<DtoImportTemplateOverview> loadImportTemplatesOverview(AsyncMonitor monitor) {
		LOGGER.info("Loading import templates overview.");
		monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_IMPORT_TEMPLATES);
		
		List<ImportTemplate> templates = importTemplateJpaRepository.findAll();
		return importTemplateToDtoImportTemplateOverviewMapper.mapAll(templates);
	}
	
	public void rename(AsyncMonitor monitor, long templateId, String newName) {
		LOGGER.info("Deleting importing template with id '{}'.", templateId);
		monitor.setStatusText(MessageKey.ASYNC_TASK_RENAME_IMPORT_TEMPLATE);
		
		Optional<ImportTemplate> importTemplate = importTemplateJpaRepository.findById(templateId);
		if (importTemplate.isPresent()) {
			importTemplate.get().setName(newName);
			importTemplateJpaRepository.save(importTemplate.get());
		}
	}
	
	public void delete(AsyncMonitor monitor, long templateId) {
		LOGGER.info("Deleting importing template with id '{}'.", templateId);
		monitor.setStatusText(MessageKey.ASYNC_TASK_DELETE_IMPORT_TEMPLATE);
		
		importTemplateDeleteController.deleteExistingTemplate(templateId);
	}
}
