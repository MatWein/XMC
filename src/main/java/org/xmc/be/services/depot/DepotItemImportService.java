package org.xmc.be.services.depot;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.common.stubs.depot.items.DepotItemImportColmn;
import org.xmc.common.stubs.depot.items.DtoDepotItemImportRow;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.fe.async.AsyncMonitor;

@Service
@Transactional
public class DepotItemImportService {
	public void importDeliveries(
			AsyncMonitor monitor,
			long depotDeliveryId,
			DtoImportData<DepotItemImportColmn> importData) {
	
		
	}
	
	public DtoImportFileValidationResult<DtoDepotItemImportRow> readAndValidateImportFile(
			AsyncMonitor monitor,
			DtoImportData<DepotItemImportColmn> importData) {
		
		return null;
	}
}
