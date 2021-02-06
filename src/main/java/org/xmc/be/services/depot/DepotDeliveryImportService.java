package org.xmc.be.services.depot;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryImportRow;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.fe.async.AsyncMonitor;

@Service
@Transactional
public class DepotDeliveryImportService {
	public void importTransactions(
			AsyncMonitor monitor,
			long depotId,
			DtoImportData<DepotDeliveryImportColmn> importData) {
	
		
	}
	
	public DtoImportFileValidationResult<DtoDepotDeliveryImportRow> readAndValidateImportFile(
			AsyncMonitor monitor,
			DtoImportData<DepotDeliveryImportColmn> importData) {
		
		return null;
	}
}
