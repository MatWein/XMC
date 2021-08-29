package io.github.matwein.xmc.be.services.depot;

import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.repositories.depot.DepotJpaRepository;
import io.github.matwein.xmc.be.services.depot.controller.importing.DepotDeliveryImportController;
import io.github.matwein.xmc.be.services.depot.controller.importing.DepotDeliveryImportLineValidator;
import io.github.matwein.xmc.be.services.depot.mapper.DepotDeliveryImportLineMapper;
import io.github.matwein.xmc.be.services.importing.controller.ImportPreparationController;
import io.github.matwein.xmc.common.services.depot.IDepotDeliveryImportService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepotDeliveryImportService implements IDepotDeliveryImportService {
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
	
	@Override
	public DtoImportFileValidationResult<DtoDepotDeliveryImportRow> readAndValidateImportFile(
			IAsyncMonitor monitor,
			DtoImportData<DepotDeliveryImportColmn> importData) {
		
		LOGGER.info("Validating depot delivery import file: {}", importData);
		return importPreparationController.readAndValidateImportFile(
				monitor,
				importData,
				depotDeliveryImportLineMapper,
				depotDeliveryImportLineValidator);
	}
	
	@Override
	public void importDeliveries(
			IAsyncMonitor monitor,
			long depotId,
			DtoImportData<DepotDeliveryImportColmn> importData) {
		
		LOGGER.info("Importing depot delivery import file: {}", importData);
		Depot depot = depotJpaRepository.getById(depotId);
		
		depotDeliveryImportController.importDeliveries(monitor, depot, importData);
	}
}
