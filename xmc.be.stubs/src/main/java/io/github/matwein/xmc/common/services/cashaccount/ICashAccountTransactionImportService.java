package io.github.matwein.xmc.common.services.cashaccount;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResult;

public interface ICashAccountTransactionImportService {
	DtoImportFileValidationResult<DtoCashAccountTransaction> readAndValidateImportFile(
			IAsyncMonitor monitor,
			DtoImportData<CashAccountTransactionImportColmn> importData);
	
	void importTransactions(
			IAsyncMonitor monitor,
			long cashAccountId,
			DtoImportData<CashAccountTransactionImportColmn> importData);
}
