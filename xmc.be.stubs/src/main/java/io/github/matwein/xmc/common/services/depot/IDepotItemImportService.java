package io.github.matwein.xmc.common.services.depot;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemImportColmn;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResult;

public interface IDepotItemImportService {
	DtoImportFileValidationResult<DtoDepotItemImportRow> readAndValidateImportFile(
			IAsyncMonitor monitor,
			DtoImportData<DepotItemImportColmn> importData);
	
	void importDeliveries(
			IAsyncMonitor monitor,
			long depotDeliveryId,
			DtoImportData<DepotItemImportColmn> importData);
}
