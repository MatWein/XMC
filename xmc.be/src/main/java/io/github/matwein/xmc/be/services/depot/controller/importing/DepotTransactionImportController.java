package io.github.matwein.xmc.be.services.depot.controller.importing;

import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotTransaction;
import io.github.matwein.xmc.be.repositories.depot.DepotTransactionJpaRepository;
import io.github.matwein.xmc.be.services.depot.controller.DepotTransactionSaveController;
import io.github.matwein.xmc.be.services.depot.mapper.DepotTransactionImportLineMapper;
import io.github.matwein.xmc.be.services.depot.mapper.DepotTransactionImportRowToDtoDepotTransactionMapper;
import io.github.matwein.xmc.be.services.importing.controller.ImportPreparationController;
import io.github.matwein.xmc.be.services.importing.controller.ImportTemplateSaveOrUpdateController;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransaction;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransactionImportRow;
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
	
	public void importDepotTransactions(IAsyncMonitor monitor, Depot depot, DtoImportData<DepotTransactionImportColmn> importData) {
		if (importData.isSaveTemplate()) {
			monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_IMPORT_TEMPLATE));
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
		
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_IMPORTING_DEPOT_TRANSACTIONS));
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
