package org.xmc.be.services.importing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.importing.ImportTemplate;
import org.xmc.be.entities.importing.ImportTemplateType;
import org.xmc.be.repositories.importing.ImportTemplateJpaRepository;
import org.xmc.be.services.importing.mapper.ImportTemplateToDtoImportTemplateMapper;
import org.xmc.common.stubs.importing.DtoImportTemplate;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.List;

@Service
@Transactional
public class ImportTemplateService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportTemplateService.class);
	
	private final ImportTemplateJpaRepository importTemplateJpaRepository;
	private final ImportTemplateToDtoImportTemplateMapper importTemplateToDtoImportTemplateMapper;
	
	@Autowired
	public ImportTemplateService(
			ImportTemplateJpaRepository importTemplateJpaRepository,
			ImportTemplateToDtoImportTemplateMapper importTemplateToDtoImportTemplateMapper) {
		
		this.importTemplateJpaRepository = importTemplateJpaRepository;
		this.importTemplateToDtoImportTemplateMapper = importTemplateToDtoImportTemplateMapper;
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
}
