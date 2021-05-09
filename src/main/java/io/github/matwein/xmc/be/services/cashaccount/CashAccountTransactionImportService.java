package io.github.matwein.xmc.be.services.cashaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import io.github.matwein.xmc.be.services.cashaccount.controller.importing.CashAccountTransactionImportController;
import io.github.matwein.xmc.be.services.cashaccount.controller.importing.CashAccountTransactionImportLineMapper;
import io.github.matwein.xmc.be.services.cashaccount.controller.importing.CashAccountTransactionImportLineValidator;
import io.github.matwein.xmc.be.services.importing.controller.ImportPreparationController;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResult;
import io.github.matwein.xmc.fe.async.AsyncMonitor;

@Service
@Transactional
public class CashAccountTransactionImportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountTransactionImportService.class);
    
	private final ImportPreparationController importPreparationController;
	private final CashAccountJpaRepository cashAccountJpaRepository;
	private final CashAccountTransactionImportController cashAccountTransactionImportController;
	private final CashAccountTransactionImportLineMapper cashAccountTransactionImportLineMapper;
	private final CashAccountTransactionImportLineValidator cashAccountTransactionImportLineValidator;
	
	@Autowired
	public CashAccountTransactionImportService(
			ImportPreparationController importPreparationController,
			CashAccountJpaRepository cashAccountJpaRepository,
			CashAccountTransactionImportController cashAccountTransactionImportController,
			CashAccountTransactionImportLineMapper cashAccountTransactionImportLineMapper,
			CashAccountTransactionImportLineValidator cashAccountTransactionImportLineValidator) {
		
		this.importPreparationController = importPreparationController;
		this.cashAccountJpaRepository = cashAccountJpaRepository;
		this.cashAccountTransactionImportController = cashAccountTransactionImportController;
		this.cashAccountTransactionImportLineMapper = cashAccountTransactionImportLineMapper;
		this.cashAccountTransactionImportLineValidator = cashAccountTransactionImportLineValidator;
	}
	
	public DtoImportFileValidationResult<DtoCashAccountTransaction> readAndValidateImportFile(
    		AsyncMonitor monitor,
		    DtoImportData<CashAccountTransactionImportColmn> importData) {
    	
        LOGGER.info("Validating cash account transaction import file: {}", importData);
        return importPreparationController.readAndValidateImportFile(
        		monitor,
		        importData,
		        cashAccountTransactionImportLineMapper,
		        cashAccountTransactionImportLineValidator);
    }
	
	public void importTransactions(
			AsyncMonitor monitor,
			long cashAccountId,
			DtoImportData<CashAccountTransactionImportColmn> importData) {
		
		LOGGER.info("Importing cash account transaction import file: {}", importData);
		CashAccount cashAccount = cashAccountJpaRepository.getOne(cashAccountId);
		
		cashAccountTransactionImportController.importTransactions(monitor, cashAccount, importData);
	}
}
