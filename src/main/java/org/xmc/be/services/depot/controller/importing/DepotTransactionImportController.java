package org.xmc.be.services.depot.controller.importing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotTransaction;
import org.xmc.be.entities.importing.ImportTemplateType;
import org.xmc.be.repositories.depot.DepotTransactionJpaRepository;
import org.xmc.be.services.depot.controller.DepotTransactionSaveController;
import org.xmc.be.services.depot.mapper.DepotTransactionImportLineMapper;
import org.xmc.be.services.depot.mapper.DepotTransactionImportRowToDtoDepotTransactionMapper;
import org.xmc.be.services.importing.controller.ImportPreparationController;
import org.xmc.be.services.importing.controller.ImportTemplateSaveOrUpdateController;
import org.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransaction;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransactionImportRow;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.common.stubs.importing.ImportType;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class DepotTransactionImportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotTransactionImportController.class);
	
	private final ImportTemplateSaveOrUpdateController importTemplateSaveOrUpdateController;
	private final ImportPreparationController importPreparationController;
	private final DepotTransactionImportLineMapper depotTransactionImportLineMapper;
	private final DepotTransactionImportLineValidator depotTransactionImportLineValidator;
	private final DepotTransactionJpaRepository depotTransactionJpaRepository;
	private final DepotTransactionSaveController depotTransactionSaveController;
	private final DepotTransactionFinder depotTransactionFinder;
	private final DepotTransactionImportRowToDtoDepotTransactionMapper depotTransactionImportRowToDtoDepotTransactionMapper;
	
	@Autowired
	public DepotTransactionImportController(
			ImportTemplateSaveOrUpdateController importTemplateSaveOrUpdateController,
			ImportPreparationController importPreparationController,
			DepotTransactionImportLineMapper depotTransactionImportLineMapper,
			DepotTransactionImportLineValidator depotTransactionImportLineValidator,
			DepotTransactionJpaRepository depotTransactionJpaRepository,
			DepotTransactionSaveController depotTransactionSaveController,
			DepotTransactionFinder depotTransactionFinder,
			DepotTransactionImportRowToDtoDepotTransactionMapper depotTransactionImportRowToDtoDepotTransactionMapper) {
		
		this.importTemplateSaveOrUpdateController = importTemplateSaveOrUpdateController;
		this.importPreparationController = importPreparationController;
		this.depotTransactionImportLineMapper = depotTransactionImportLineMapper;
		this.depotTransactionImportLineValidator = depotTransactionImportLineValidator;
		this.depotTransactionJpaRepository = depotTransactionJpaRepository;
		this.depotTransactionSaveController = depotTransactionSaveController;
		this.depotTransactionFinder = depotTransactionFinder;
		this.depotTransactionImportRowToDtoDepotTransactionMapper = depotTransactionImportRowToDtoDepotTransactionMapper;
	}
	
	public void importDepotTransactions(AsyncMonitor monitor, Depot depot, DtoImportData<DepotTransactionImportColmn> importData) {
		if (importData.isSaveTemplate()) {
			monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_IMPORT_TEMPLATE);
			importTemplateSaveOrUpdateController.saveTemplate(
					importData.getTemplateToSaveName(),
					ImportTemplateType.DEPOT_TRANSACTION,
					importData.getImportType(),
					importData.getCsvSeparator(),
					importData.getStartWithLine(),
					importData.getColmuns(),
					importData.getEncoding());
		}
		
		DtoImportFileValidationResult<DtoDepotTransactionImportRow> fileValidationResult = importPreparationController.readAndValidateImportFile(
				monitor,
				importData,
				depotTransactionImportLineMapper,
				depotTransactionImportLineValidator);
		
		int processItemCount = fileValidationResult.getSuccessfullyReadLines().size();
		int processedItems = 0;
		
		monitor.setStatusText(MessageKey.ASYNC_TASK_IMPORTING_DEPOT_TRANSACTIONS);
		monitor.setProgressByItemCount(processedItems, processItemCount);
		
		LOGGER.info("Got {} depot transaction rows to import.", processItemCount);
		
		List<DepotTransaction> existingDepotTransactions = depotTransactionJpaRepository.findByDepotAndDeletionDateIsNull(depot);
		
		for (DtoDepotTransactionImportRow depotTransactionImportRow : fileValidationResult.getSuccessfullyReadLines()) {
			Optional<DepotTransaction> existingDepotTransaction = depotTransactionFinder.findExistingTransaction(existingDepotTransactions, depotTransactionImportRow);
			DtoDepotTransaction dtoDepotTransaction = depotTransactionImportRowToDtoDepotTransactionMapper.map(depotTransactionImportRow);
			
			if (importData.getImportType() == ImportType.ADD_ALL) {
				depotTransactionSaveController.saveOrUpdate(depot, dtoDepotTransaction);
			} else if (importData.getImportType() == ImportType.ADD_AND_UPDATE_EXISTING) {
				if (existingDepotTransaction.isPresent()) {
					existingDepotTransaction.get().setDeletionDate(LocalDateTime.now());
					depotTransactionJpaRepository.save(existingDepotTransaction.get());
				}
				depotTransactionSaveController.saveOrUpdate(depot, dtoDepotTransaction);
			} else if (importData.getImportType() == ImportType.ADD_ONLY && existingDepotTransaction.isEmpty()) {
				depotTransactionSaveController.saveOrUpdate(depot, dtoDepotTransaction);
			}
			
			monitor.setProgressByItemCount(++processedItems, processItemCount);
		}
	}
}
