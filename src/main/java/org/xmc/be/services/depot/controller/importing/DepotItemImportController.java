package org.xmc.be.services.depot.controller.importing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.be.entities.depot.DepotItem;
import org.xmc.be.entities.importing.ImportTemplateType;
import org.xmc.be.repositories.depot.DepotItemJpaRepository;
import org.xmc.be.services.depot.controller.DeliverySaldoUpdatingController;
import org.xmc.be.services.depot.controller.DepotItemSaveController;
import org.xmc.be.services.depot.mapper.DepotItemImportLineMapper;
import org.xmc.be.services.depot.mapper.DepotItemImportRowToDtoDepotItemMapper;
import org.xmc.be.services.importing.controller.ImportPreparationController;
import org.xmc.be.services.importing.controller.ImportTemplateSaveOrUpdateController;
import org.xmc.common.stubs.depot.items.DepotItemImportColmn;
import org.xmc.common.stubs.depot.items.DtoDepotItem;
import org.xmc.common.stubs.depot.items.DtoDepotItemImportRow;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.common.stubs.importing.ImportType;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

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
	
	public void importDepotItems(AsyncMonitor monitor, DepotDelivery depotDelivery, DtoImportData<DepotItemImportColmn> importData) {
		if (importData.isSaveTemplate()) {
			monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_IMPORT_TEMPLATE);
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
		
		monitor.setStatusText(MessageKey.ASYNC_TASK_IMPORTING_DEPOT_ITEMS);
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
