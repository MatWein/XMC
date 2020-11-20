package org.xmc.be.services.cashaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.services.cashaccount.controller.importing.CashAccountTransactionImportController;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.fe.async.AsyncMonitor;

@Service
@Transactional
public class CashAccountTransactionImportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountTransactionImportService.class);
    
	private final CashAccountTransactionImportController cashAccountTransactionImportController;
	
	@Autowired
	public CashAccountTransactionImportService(CashAccountTransactionImportController cashAccountTransactionImportController) {
		this.cashAccountTransactionImportController = cashAccountTransactionImportController;
	}
	
	public DtoImportFileValidationResult<DtoCashAccountTransaction> readAndValidateImportFile(
    		AsyncMonitor monitor,
		    DtoImportData<CashAccountTransactionImportColmn> importData) {
    	
        LOGGER.info("Validating cash account transaction import file: {}", importData);
        return cashAccountTransactionImportController.readAndValidateImportFile(monitor, importData);
    }
}
