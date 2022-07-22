package io.github.matwein.xmc.be.services.cashaccount;

import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import io.github.matwein.xmc.be.services.cashaccount.controller.importing.CashAccountTransactionImportController;
import io.github.matwein.xmc.be.services.cashaccount.controller.importing.CashAccountTransactionImportLineMapper;
import io.github.matwein.xmc.be.services.cashaccount.controller.importing.CashAccountTransactionImportLineValidator;
import io.github.matwein.xmc.be.services.importing.controller.ImportPreparationController;
import io.github.matwein.xmc.common.services.cashaccount.ICashAccountTransactionImportService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CashAccountTransactionImportService implements ICashAccountTransactionImportService {
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
	
	@Override
	public DtoImportFileValidationResult<DtoCashAccountTransaction> readAndValidateImportFile(
    		IAsyncMonitor monitor,
		    DtoImportData<CashAccountTransactionImportColmn> importData) {
    	
        LOGGER.info("Validating cash account transaction import file: {}", importData);
        return importPreparationController.readAndValidateImportFile(
        		monitor,
		        importData,
		        cashAccountTransactionImportLineMapper,
		        cashAccountTransactionImportLineValidator);
    }
	
    @Override
	public void importTransactions(
			IAsyncMonitor monitor,
			long cashAccountId,
			DtoImportData<CashAccountTransactionImportColmn> importData) {
		
		LOGGER.info("Importing cash account transaction import file: {}", importData);
		CashAccount cashAccount = cashAccountJpaRepository.getReferenceById(cashAccountId);
		
		cashAccountTransactionImportController.importTransactions(monitor, cashAccount, importData);
	}
}
