package io.github.matwein.xmc.be.services.importing;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.entities.importing.ImportTemplate;
import io.github.matwein.xmc.be.repositories.importing.ImportTemplateJpaRepository;
import io.github.matwein.xmc.be.services.importing.controller.ImportTemplateDeleteController;
import io.github.matwein.xmc.be.services.importing.controller.ImportTemplateRenameController;
import io.github.matwein.xmc.be.services.importing.mapper.ImportTemplateToDtoImportTemplateMapper;
import io.github.matwein.xmc.be.services.importing.mapper.ImportTemplateToDtoImportTemplateOverviewMapper;
import io.github.matwein.xmc.common.services.imoprting.IImportTemplateService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.importing.DtoImportTemplate;
import io.github.matwein.xmc.common.stubs.importing.DtoImportTemplateOverview;
import io.github.matwein.xmc.common.stubs.importing.ImportTemplateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ImportTemplateService implements IImportTemplateService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportTemplateService.class);
	
	private final ImportTemplateJpaRepository importTemplateJpaRepository;
	private final ImportTemplateToDtoImportTemplateMapper importTemplateToDtoImportTemplateMapper;
	private final ImportTemplateToDtoImportTemplateOverviewMapper importTemplateToDtoImportTemplateOverviewMapper;
	private final ImportTemplateDeleteController importTemplateDeleteController;
	private final ImportTemplateRenameController importTemplateRenameController;
	
	@Autowired
	public ImportTemplateService(
			ImportTemplateJpaRepository importTemplateJpaRepository,
			ImportTemplateToDtoImportTemplateMapper importTemplateToDtoImportTemplateMapper,
			ImportTemplateToDtoImportTemplateOverviewMapper importTemplateToDtoImportTemplateOverviewMapper,
			ImportTemplateDeleteController importTemplateDeleteController,
			ImportTemplateRenameController importTemplateRenameController) {
		
		this.importTemplateJpaRepository = importTemplateJpaRepository;
		this.importTemplateToDtoImportTemplateMapper = importTemplateToDtoImportTemplateMapper;
		this.importTemplateToDtoImportTemplateOverviewMapper = importTemplateToDtoImportTemplateOverviewMapper;
		this.importTemplateDeleteController = importTemplateDeleteController;
		this.importTemplateRenameController = importTemplateRenameController;
	}
	
	@Override
	public <T extends Enum<T>> List<DtoImportTemplate<T>> loadImportTemplates(
			IAsyncMonitor monitor,
			ImportTemplateType templateType,
			Class<T> columnType) {
		
		LOGGER.info("Loading import templates for enum type '{}'.", columnType.getSimpleName());
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_IMPORT_TEMPLATES));
		
		List<ImportTemplate> templates = importTemplateJpaRepository.findByType(templateType);
		return importTemplateToDtoImportTemplateMapper.mapAll(templates, columnType);
	}
	
	@Override
	public List<DtoImportTemplateOverview> loadImportTemplatesOverview(IAsyncMonitor monitor) {
		LOGGER.info("Loading import templates overview.");
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_IMPORT_TEMPLATES));
		
		List<ImportTemplate> templates = importTemplateJpaRepository.findAll();
		return importTemplateToDtoImportTemplateOverviewMapper.mapAll(templates);
	}
	
	@Override
	public boolean rename(IAsyncMonitor monitor, long templateId, String newName) {
		LOGGER.info("Deleting importing template with id '{}'.", templateId);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_RENAME_IMPORT_TEMPLATE));
		
		return importTemplateRenameController.rename(templateId, newName);
	}
	
	@Override
	public void delete(IAsyncMonitor monitor, long templateId) {
		LOGGER.info("Deleting importing template with id '{}'.", templateId);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_DELETE_IMPORT_TEMPLATE));
		
		importTemplateDeleteController.deleteExistingTemplate(templateId);
	}
}
