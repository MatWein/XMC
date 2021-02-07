package org.xmc.be.services.depot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.repositories.depot.DepotJpaRepository;
import org.xmc.be.services.depot.controller.importing.DepotDeliveryImportController;
import org.xmc.be.services.depot.controller.importing.DepotDeliveryImportLineMapper;
import org.xmc.be.services.depot.controller.importing.DepotDeliveryImportLineValidator;
import org.xmc.be.services.importing.controller.ImportPreparationController;
import org.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryImportRow;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.fe.async.AsyncMonitor;

@Service
@Transactional
public class DepotDeliveryImportService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotDeliveryImportService.class);
	
	private final DepotDeliveryImportLineMapper depotDeliveryImportLineMapper;
	private final DepotDeliveryImportLineValidator depotDeliveryImportLineValidator;
	private final ImportPreparationController importPreparationController;
	private final DepotJpaRepository depotJpaRepository;
	private final DepotDeliveryImportController depotDeliveryImportController;
	
	@Autowired
	public DepotDeliveryImportService(
			DepotDeliveryImportLineMapper depotDeliveryImportLineMapper,
			DepotDeliveryImportLineValidator depotDeliveryImportLineValidator,
			ImportPreparationController importPreparationController,
			DepotJpaRepository depotJpaRepository,
			DepotDeliveryImportController depotDeliveryImportController) {
		
		this.depotDeliveryImportLineMapper = depotDeliveryImportLineMapper;
		this.depotDeliveryImportLineValidator = depotDeliveryImportLineValidator;
		this.importPreparationController = importPreparationController;
		this.depotJpaRepository = depotJpaRepository;
		this.depotDeliveryImportController = depotDeliveryImportController;
	}
	
	public DtoImportFileValidationResult<DtoDepotDeliveryImportRow> readAndValidateImportFile(
			AsyncMonitor monitor,
			DtoImportData<DepotDeliveryImportColmn> importData) {
		
		LOGGER.info("Validating depot delivery import file: {}", importData);
		return importPreparationController.readAndValidateImportFile(
				monitor,
				importData,
				depotDeliveryImportLineMapper,
				depotDeliveryImportLineValidator);
	}
	
	public void importDeliveries(
			AsyncMonitor monitor,
			long depotId,
			DtoImportData<DepotDeliveryImportColmn> importData) {
		
		LOGGER.info("Importing depot delivery import file: {}", importData);
		Depot depot = depotJpaRepository.getOne(depotId);
		
		depotDeliveryImportController.importTransactions(monitor, depot, importData);
	}
}
