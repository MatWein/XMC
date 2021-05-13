package io.github.matwein.xmc.common.services.depot;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResult;

public interface IDepotDeliveryImportService {
	DtoImportFileValidationResult<DtoDepotDeliveryImportRow> readAndValidateImportFile(
			IAsyncMonitor monitor,
			DtoImportData<DepotDeliveryImportColmn> importData);
	
	void importDeliveries(
			IAsyncMonitor monitor,
			long depotId,
			DtoImportData<DepotDeliveryImportColmn> importData);
}
