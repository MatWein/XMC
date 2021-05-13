package io.github.matwein.xmc.be.services.cashaccount.controller.importing;

import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import io.github.matwein.xmc.be.services.cashaccount.controller.CashAccountTransactionSaveController;
import io.github.matwein.xmc.be.services.importing.controller.ImportPreparationController;
import io.github.matwein.xmc.be.services.importing.controller.ImportTemplateSaveOrUpdateController;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.ImportTemplateType;
import io.github.matwein.xmc.common.stubs.importing.ImportType;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CashAccountTransactionImportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountTransactionImportController.class);
	
	private final ImportTemplateSaveOrUpdateController importTemplateSaveOrUpdateController;
	private final ImportPreparationController importPreparationController;
	private final CashAccountTransactionSaveController cashAccountTransactionSaveController;
	private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
	private final CashAccountTransactionFinder cashAccountTransactionFinder;
	private final CashAccountTransactionImportLineMapper cashAccountTransactionImportLineMapper;
	private final CashAccountTransactionImportLineValidator cashAccountTransactionImportLineValidator;
	
	@Autowired
	public CashAccountTransactionImportController(
			ImportTemplateSaveOrUpdateController importTemplateSaveOrUpdateController,
			ImportPreparationController importPreparationController,
			CashAccountTransactionSaveController cashAccountTransactionSaveController,
			CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository,
			CashAccountTransactionFinder cashAccountTransactionFinder,
			CashAccountTransactionImportLineMapper cashAccountTransactionImportLineMapper,
			CashAccountTransactionImportLineValidator cashAccountTransactionImportLineValidator) {
		
		this.importTemplateSaveOrUpdateController = importTemplateSaveOrUpdateController;
		this.importPreparationController = importPreparationController;
		this.cashAccountTransactionSaveController = cashAccountTransactionSaveController;
		this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
		this.cashAccountTransactionFinder = cashAccountTransactionFinder;
		this.cashAccountTransactionImportLineMapper = cashAccountTransactionImportLineMapper;
		this.cashAccountTransactionImportLineValidator = cashAccountTransactionImportLineValidator;
	}
	
	public void importTransactions(IAsyncMonitor monitor, CashAccount cashAccount, DtoImportData<CashAccountTransactionImportColmn> importData) {
		if (importData.isSaveTemplate()) {
			monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_IMPORT_TEMPLATE));
			importTemplateSaveOrUpdateController.saveTemplate(
					importData.getTemplateToSaveName(),
					ImportTemplateType.CASH_ACCOUNT_TRANSACTION,
					importData.getImportType(),
					importData.getCsvSeparator(),
					importData.getStartWithLine(),
					importData.getColmuns(),
					importData.getEncoding());
		}
		
		var fileValidationResult = importPreparationController.readAndValidateImportFile(
				monitor,
				importData,
				cashAccountTransactionImportLineMapper,
				cashAccountTransactionImportLineValidator);
		
		int processItemCount = fileValidationResult.getSuccessfullyReadLines().size();
		int processedItems = 0;
		
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_IMPORTING_TRANSACTIONS));
		monitor.setProgressByItemCount(processedItems, processItemCount);
		
		LOGGER.info("Got {} transactions to import.", processItemCount);
		
		List<CashAccountTransaction> allTransactions = cashAccountTransactionJpaRepository.findByCashAccountAndDeletionDateIsNull(cashAccount);
		
		for (DtoCashAccountTransaction transactionToImport : fileValidationResult.getSuccessfullyReadLines()) {
			Optional<CashAccountTransaction> existingTransaction = cashAccountTransactionFinder.findExistingTransaction(allTransactions, transactionToImport);
			
			if (importData.getImportType() == ImportType.ADD_ALL) {
				cashAccountTransactionSaveController.saveOrUpdate(cashAccount, transactionToImport);
			} else if (importData.getImportType() == ImportType.ADD_AND_UPDATE_EXISTING) {
				if (existingTransaction.isPresent()) {
					transactionToImport.setId(existingTransaction.get().getId());
				}
				cashAccountTransactionSaveController.saveOrUpdate(cashAccount, transactionToImport);
			} else if (importData.getImportType() == ImportType.ADD_ONLY && existingTransaction.isEmpty()) {
				cashAccountTransactionSaveController.saveOrUpdate(cashAccount, transactionToImport);
			}
			
			monitor.setProgressByItemCount(++processedItems, processItemCount);
		}
	}
}
