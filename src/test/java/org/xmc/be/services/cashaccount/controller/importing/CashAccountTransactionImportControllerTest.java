package org.xmc.be.services.cashaccount.controller.importing;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.common.stubs.importing.CsvSeparator;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.fe.async.AsyncMonitor;

import java.io.File;

class CashAccountTransactionImportControllerTest extends IntegrationTest {
	@Autowired
	private CashAccountTransactionImportController controller;
	
	@Mock
	private AsyncMonitor asyncMonitor;
	
	@Test
	void readAndValidateImportFile_Excel() {
		File fileToImport = new File(getClass().getResource("/importing/Umsatzanzeige_DE11100111171110921111_20200828.xlsx").getFile());
		
		DtoImportData<CashAccountTransactionImportColmn> importData = new DtoImportData<>();
		importData.setFileToImport(fileToImport);
		importData.setStartWithLine(16);
		
		DtoImportFileValidationResult<DtoCashAccountTransaction> result = controller.readAndValidateImportFile(asyncMonitor, importData);
		
		
	}
	
	@Test
	void readAndValidateImportFile_Csv() {
		File fileToImport = new File(getClass().getResource("/importing/Umsatzanzeige_DE11100111171110921111_20200828.csv").getFile());
		
		DtoImportData<CashAccountTransactionImportColmn> importData = new DtoImportData<>();
		importData.setFileToImport(fileToImport);
		importData.setStartWithLine(16);
		importData.setCsvSeparator(CsvSeparator.SEMICOLON);
		
		DtoImportFileValidationResult<DtoCashAccountTransaction> result = controller.readAndValidateImportFile(asyncMonitor, importData);
		
		
	}
}