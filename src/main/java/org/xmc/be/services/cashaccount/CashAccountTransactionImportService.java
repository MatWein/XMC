package org.xmc.be.services.cashaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import org.xmc.be.services.cashaccount.controller.importing.CashAccountTransactionImportController;
import org.xmc.be.services.cashaccount.controller.importing.CashAccountTransactionImportPreparationController;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.fe.async.AsyncMonitor;

@Service
@Transactional
public class CashAccountTransactionImportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountTransactionImportService.class);
    
	private final CashAccountTransactionImportPreparationController cashAccountTransactionImportPreparationController;
	private final CashAccountJpaRepository cashAccountJpaRepository;
	private final CashAccountTransactionImportController cashAccountTransactionImportController;
	
	@Autowired
	public CashAccountTransactionImportService(
			CashAccountTransactionImportPreparationController cashAccountTransactionImportPreparationController,
			CashAccountJpaRepository cashAccountJpaRepository,
			CashAccountTransactionImportController cashAccountTransactionImportController) {
		
		this.cashAccountTransactionImportPreparationController = cashAccountTransactionImportPreparationController;
		this.cashAccountJpaRepository = cashAccountJpaRepository;
		this.cashAccountTransactionImportController = cashAccountTransactionImportController;
	}
	
	public DtoImportFileValidationResult<DtoCashAccountTransaction> readAndValidateImportFile(
    		AsyncMonitor monitor,
		    DtoImportData<CashAccountTransactionImportColmn> importData) {
    	
        LOGGER.info("Validating cash account transaction import file: {}", importData);
        return cashAccountTransactionImportPreparationController.readAndValidateImportFile(monitor, importData);
    }
	
	public void importTransactions(
			AsyncMonitor monitor,
			long cashAccountId,
			DtoImportData<CashAccountTransactionImportColmn> importData) throws Exception {
		
		LOGGER.info("Importing cash account transaction import file: {}", importData);
		CashAccount cashAccount = cashAccountJpaRepository.getOne(cashAccountId);
		
		cashAccountTransactionImportController.importTransactions(monitor, cashAccount, importData);
	}
}
