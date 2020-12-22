package org.xmc.be.services.cashaccount.controller.importing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.common.importing.controller.ImportTemplateSaveOrUpdateController;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.be.entities.importing.ImportTemplateType;
import org.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import org.xmc.be.services.cashaccount.controller.CashAccountTransactionSaveController;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.ImportType;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.List;
import java.util.Optional;

@Component
public class CashAccountTransactionImportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountTransactionImportController.class);
	
	private final ImportTemplateSaveOrUpdateController importTemplateSaveOrUpdateController;
	private final CashAccountTransactionImportPreparationController cashAccountTransactionImportPreparationController;
	private final CashAccountTransactionSaveController cashAccountTransactionSaveController;
	private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
	
	@Autowired
	public CashAccountTransactionImportController(
			ImportTemplateSaveOrUpdateController importTemplateSaveOrUpdateController,
			CashAccountTransactionImportPreparationController cashAccountTransactionImportPreparationController,
			CashAccountTransactionSaveController cashAccountTransactionSaveController,
			CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository) {
		
		this.importTemplateSaveOrUpdateController = importTemplateSaveOrUpdateController;
		this.cashAccountTransactionImportPreparationController = cashAccountTransactionImportPreparationController;
		this.cashAccountTransactionSaveController = cashAccountTransactionSaveController;
		this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
	}
	
	public void importTransactions(AsyncMonitor monitor, CashAccount cashAccount, DtoImportData<CashAccountTransactionImportColmn> importData) throws Exception {
		if (importData.isSaveTemplate()) {
			monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_IMPORT_TEMPLATE);
			importTemplateSaveOrUpdateController.saveTemplate(
					importData.getTemplateToSaveName(),
					ImportTemplateType.CASH_ACCOUNT_TRANSACTION,
					importData.getImportType(),
					importData.getCsvSeparator(),
					importData.getStartWithLine(),
					importData.getColmuns());
		}
		
		var fileValidationResult = cashAccountTransactionImportPreparationController.readAndValidateWithoutErrorHandling(monitor, importData);
		
		int processItemCount = fileValidationResult.getSuccessfullyReadLines().size();
		int processedItems = 0;
		
		monitor.setStatusText(MessageKey.ASYNC_TASK_IMPORTING_TRANSACTIONS);
		monitor.setProgressByItemCount(processedItems, processItemCount);
		
		LOGGER.info("Got {} transactions to import.", processItemCount);
		
		List<CashAccountTransaction> allTransactions = cashAccountTransactionJpaRepository.findByCashAccount(cashAccount);
		
		for (DtoCashAccountTransaction transactionToImport : fileValidationResult.getSuccessfullyReadLines()) {
			Optional<CashAccountTransaction> existingTransaction = findExistingTransaction(allTransactions, transactionToImport);
			
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
	
	private Optional<CashAccountTransaction> findExistingTransaction(
			List<CashAccountTransaction> allTransactions,
			DtoCashAccountTransaction transactionToFind) {
		
		return Optional.empty();
	}
}
