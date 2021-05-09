package io.github.matwein.xmc.be.services.depot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.repositories.depot.DepotJpaRepository;
import io.github.matwein.xmc.be.services.depot.controller.importing.DepotDeliveryImportController;
import io.github.matwein.xmc.be.services.depot.controller.importing.DepotDeliveryImportLineValidator;
import io.github.matwein.xmc.be.services.depot.mapper.DepotDeliveryImportLineMapper;
import io.github.matwein.xmc.be.services.importing.controller.ImportPreparationController;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResult;
import io.github.matwein.xmc.fe.async.AsyncMonitor;

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
		
		depotDeliveryImportController.importDeliveries(monitor, depot, importData);
	}
}
