package io.github.matwein.xmc.be.services.depot.controller.importing;

import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.be.entities.depot.DepotItem;
import io.github.matwein.xmc.be.repositories.depot.DepotItemJpaRepository;
import io.github.matwein.xmc.be.services.depot.controller.DeliverySaldoUpdatingController;
import io.github.matwein.xmc.be.services.depot.controller.DepotItemSaveController;
import io.github.matwein.xmc.be.services.depot.mapper.DepotItemImportLineMapper;
import io.github.matwein.xmc.be.services.depot.mapper.DepotItemImportRowToDtoDepotItemMapper;
import io.github.matwein.xmc.be.services.importing.controller.ImportPreparationController;
import io.github.matwein.xmc.be.services.importing.controller.ImportTemplateSaveOrUpdateController;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemImportColmn;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItem;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResult;
import io.github.matwein.xmc.common.stubs.importing.ImportTemplateType;
import io.github.matwein.xmc.common.stubs.importing.ImportType;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class DepotItemImportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotItemImportController.class);
	
	private final ImportTemplateSaveOrUpdateController importTemplateSaveOrUpdateController;
	private final ImportPreparationController importPreparationController;
	private final DepotItemSaveController depotItemSaveController;
	private final DeliverySaldoUpdatingController deliverySaldoUpdatingController;
	private final DepotItemImportLineMapper depotItemImportLineMapper;
	private final DepotItemImportLineValidator depotItemImportLineValidator;
	private final DepotItemJpaRepository depotItemJpaRepository;
	private final DepotItemImportRowToDtoDepotItemMapper depotItemImportRowToDtoDepotItemMapper;
	private final DepotItemFinder depotItemFinder;
	
	@Autowired
	public DepotItemImportController(
			ImportTemplateSaveOrUpdateController importTemplateSaveOrUpdateController,
			ImportPreparationController importPreparationController,
			DepotItemSaveController depotItemSaveController,
			DeliverySaldoUpdatingController deliverySaldoUpdatingController,
			DepotItemImportLineMapper depotItemImportLineMapper,
			DepotItemImportLineValidator depotItemImportLineValidator,
			DepotItemJpaRepository depotItemJpaRepository,
			DepotItemImportRowToDtoDepotItemMapper depotItemImportRowToDtoDepotItemMapper,
			DepotItemFinder depotItemFinder) {
		
		this.importTemplateSaveOrUpdateController = importTemplateSaveOrUpdateController;
		this.importPreparationController = importPreparationController;
		this.depotItemSaveController = depotItemSaveController;
		this.deliverySaldoUpdatingController = deliverySaldoUpdatingController;
		this.depotItemImportLineMapper = depotItemImportLineMapper;
		this.depotItemImportLineValidator = depotItemImportLineValidator;
		this.depotItemJpaRepository = depotItemJpaRepository;
		this.depotItemImportRowToDtoDepotItemMapper = depotItemImportRowToDtoDepotItemMapper;
		this.depotItemFinder = depotItemFinder;
	}
	
	public void importDepotItems(IAsyncMonitor monitor, DepotDelivery depotDelivery, DtoImportData<DepotItemImportColmn> importData) {
		if (importData.isSaveTemplate()) {
			monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_IMPORT_TEMPLATE));
			importTemplateSaveOrUpdateController.saveTemplate(
					importData.getTemplateToSaveName(),
					ImportTemplateType.DEPOT_ITEM,
					importData.getImportType(),
					importData.getCsvSeparator(),
					importData.getStartWithLine(),
					importData.getColmuns(),
					importData.getEncoding());
		}
		
		DtoImportFileValidationResult<DtoDepotItemImportRow> fileValidationResult = importPreparationController.readAndValidateImportFile(
				monitor,
				importData,
				depotItemImportLineMapper,
				depotItemImportLineValidator);
		
		int processItemCount = fileValidationResult.getSuccessfullyReadLines().size();
		int processedItems = 0;
		
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_IMPORTING_DEPOT_ITEMS));
		monitor.setProgressByItemCount(processedItems, processItemCount);
		
		LOGGER.info("Got {} depot item rows to import.", processItemCount);
		
		List<DepotItem> existingDepotItems = depotItemJpaRepository.findByDeliveryAndDeletionDateIsNull(depotDelivery);
		
		for (DtoDepotItemImportRow depotItemImportRow : fileValidationResult.getSuccessfullyReadLines()) {
			Optional<DepotItem> existingDepotItem = depotItemFinder.findExistingItem(existingDepotItems, depotItemImportRow);
			DtoDepotItem dtoDepotItem = depotItemImportRowToDtoDepotItemMapper.map(depotItemImportRow);
			
			if (importData.getImportType() == ImportType.ADD_ALL) {
				depotItemSaveController.saveOrUpdate(depotDelivery, dtoDepotItem);
			} else if (importData.getImportType() == ImportType.ADD_AND_UPDATE_EXISTING) {
				if (existingDepotItem.isPresent()) {
					existingDepotItem.get().setDeletionDate(LocalDateTime.now());
					depotItemJpaRepository.save(existingDepotItem.get());
				}
				depotItemSaveController.saveOrUpdate(depotDelivery, dtoDepotItem);
			} else if (importData.getImportType() == ImportType.ADD_ONLY && existingDepotItem.isEmpty()) {
				depotItemSaveController.saveOrUpdate(depotDelivery, dtoDepotItem);
			}
			
			monitor.setProgressByItemCount(++processedItems, processItemCount);
		}
		
		deliverySaldoUpdatingController.updateDeliverySaldo(depotDelivery);
	}
}
