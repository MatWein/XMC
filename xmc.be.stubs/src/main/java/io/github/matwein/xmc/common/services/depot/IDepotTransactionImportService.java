package io.github.matwein.xmc.common.services.depot;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransactionImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResult;

public interface IDepotTransactionImportService {
	DtoImportFileValidationResult<DtoDepotTransactionImportRow> readAndValidateImportFile(
			IAsyncMonitor monitor,
			DtoImportData<DepotTransactionImportColmn> importData);
	
	void importTransactions(IAsyncMonitor monitor, long depotId, DtoImportData<DepotTransactionImportColmn> importData);
}
