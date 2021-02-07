package org.xmc.be.services.depot.controller.importing;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.be.entities.importing.ImportTemplateType;
import org.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import org.xmc.be.services.depot.controller.DeliverySaldoUpdatingController;
import org.xmc.be.services.depot.controller.DepotDeliverySaveController;
import org.xmc.be.services.depot.controller.DepotItemSaveController;
import org.xmc.be.services.depot.mapper.DtoDepotDeliveryImportRowToDtoDepotItemMapper;
import org.xmc.be.services.importing.controller.ImportPreparationController;
import org.xmc.be.services.importing.controller.ImportTemplateSaveOrUpdateController;
import org.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDelivery;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryImportRow;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.common.stubs.importing.ImportType;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

@Component
public class DepotDeliveryImportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotDeliveryImportController.class);
	
	private final ImportTemplateSaveOrUpdateController importTemplateSaveOrUpdateController;
	private final ImportPreparationController importPreparationController;
	private final DepotDeliveryImportLineMapper depotDeliveryImportLineMapper;
	private final DepotDeliveryImportLineValidator depotDeliveryImportLineValidator;
	private final DepotDeliveryJpaRepository depotDeliveryJpaRepository;
	private final DepotDeliverySaveController depotDeliverySaveController;
	private final DepotItemSaveController depotItemSaveController;
	private final DepotDeliveryFinder depotDeliveryFinder;
	private final DtoDepotDeliveryImportRowToDtoDepotItemMapper dtoDepotDeliveryImportRowToDtoDepotItemMapper;
	private final DeliverySaldoUpdatingController deliverySaldoUpdatingController;
	
	@Autowired
	public DepotDeliveryImportController(
			ImportTemplateSaveOrUpdateController importTemplateSaveOrUpdateController,
			ImportPreparationController importPreparationController,
			DepotDeliveryImportLineMapper depotDeliveryImportLineMapper,
			DepotDeliveryImportLineValidator depotDeliveryImportLineValidator,
			DepotDeliveryJpaRepository depotDeliveryJpaRepository,
			DepotDeliverySaveController depotDeliverySaveController,
			DepotItemSaveController depotItemSaveController,
			DepotDeliveryFinder depotDeliveryFinder,
			DtoDepotDeliveryImportRowToDtoDepotItemMapper dtoDepotDeliveryImportRowToDtoDepotItemMapper,
			DeliverySaldoUpdatingController deliverySaldoUpdatingController) {
		
		this.importTemplateSaveOrUpdateController = importTemplateSaveOrUpdateController;
		this.importPreparationController = importPreparationController;
		this.depotDeliveryImportLineMapper = depotDeliveryImportLineMapper;
		this.depotDeliveryImportLineValidator = depotDeliveryImportLineValidator;
		this.depotDeliveryJpaRepository = depotDeliveryJpaRepository;
		this.depotDeliverySaveController = depotDeliverySaveController;
		this.depotItemSaveController = depotItemSaveController;
		this.depotDeliveryFinder = depotDeliveryFinder;
		this.dtoDepotDeliveryImportRowToDtoDepotItemMapper = dtoDepotDeliveryImportRowToDtoDepotItemMapper;
		this.deliverySaldoUpdatingController = deliverySaldoUpdatingController;
	}
	
	public void importTransactions(AsyncMonitor monitor, Depot depot, DtoImportData<DepotDeliveryImportColmn> importData) {
		if (importData.isSaveTemplate()) {
			monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_IMPORT_TEMPLATE);
			importTemplateSaveOrUpdateController.saveTemplate(
					importData.getTemplateToSaveName(),
					ImportTemplateType.DEPOT_DELIVERY,
					importData.getImportType(),
					importData.getCsvSeparator(),
					importData.getStartWithLine(),
					importData.getColmuns(),
					importData.getEncoding());
		}
		
		DtoImportFileValidationResult<DtoDepotDeliveryImportRow> fileValidationResult = importPreparationController.readAndValidateImportFile(
				monitor,
				importData,
				depotDeliveryImportLineMapper,
				depotDeliveryImportLineValidator);
		
		int processItemCount = fileValidationResult.getSuccessfullyReadLines().size();
		int processedItems = 0;
		
		monitor.setStatusText(MessageKey.ASYNC_TASK_IMPORTING_DEPOT_DELIVERIES);
		monitor.setProgressByItemCount(processedItems, processItemCount);
		
		LOGGER.info("Got {} delivery rows to import.", processItemCount);
		
		List<DepotDelivery> existingDeliveries = depotDeliveryJpaRepository.findByDepotAndDeletionDateIsNull(depot);
		
		Multimap<LocalDateTime, DtoDepotDeliveryImportRow> deliveriesToImport = Multimaps.index(fileValidationResult.getSuccessfullyReadLines(), DtoDepotDeliveryImportRow::getDeliveryDate);
		
		for (Entry<LocalDateTime, Collection<DtoDepotDeliveryImportRow>> deliveryToImport : deliveriesToImport.asMap().entrySet()) {
			LocalDateTime deliveryDate = deliveryToImport.getKey();
			Collection<DtoDepotDeliveryImportRow> depotItems = deliveryToImport.getValue();
			
			Optional<DepotDelivery> existingDelivery = depotDeliveryFinder.findExistingDeliveries(existingDeliveries, deliveryDate);
			
			if (importData.getImportType() == ImportType.ADD_ALL) {
				saveNewDeliveryWithItems(depot, depotItems, deliveryDate);
			} else if (importData.getImportType() == ImportType.ADD_AND_UPDATE_EXISTING) {
				if (existingDelivery.isPresent()) {
					existingDelivery.get().setDeletionDate(LocalDateTime.now());
					depotDeliveryJpaRepository.save(existingDelivery.get());
				}
				saveNewDeliveryWithItems(depot, depotItems, deliveryDate);
			} else if (importData.getImportType() == ImportType.ADD_ONLY && existingDelivery.isEmpty()) {
				saveNewDeliveryWithItems(depot, depotItems, deliveryDate);
			}
			
			processedItems += depotItems.size();
			monitor.setProgressByItemCount(processedItems, processItemCount);
		}
	}
	
	private void saveNewDeliveryWithItems(Depot depot, Collection<DtoDepotDeliveryImportRow> depotItems, LocalDateTime deliveryDate) {
		DepotDelivery depotDelivery = depotDeliverySaveController.saveOrUpdate(depot, new DtoDepotDelivery(deliveryDate));
		
		for (DtoDepotDeliveryImportRow dtoDepotDeliveryImportRow : depotItems) {
			depotItemSaveController.saveOrUpdate(depotDelivery, dtoDepotDeliveryImportRowToDtoDepotItemMapper.map(dtoDepotDeliveryImportRow));
		}
		
		deliverySaldoUpdatingController.updateDeliverySaldo(depotDelivery);
	}
}
