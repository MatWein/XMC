package io.github.matwein.xmc.be.services.depot;

import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import io.github.matwein.xmc.be.services.depot.controller.importing.DepotItemImportController;
import io.github.matwein.xmc.be.services.depot.controller.importing.DepotItemImportLineValidator;
import io.github.matwein.xmc.be.services.depot.mapper.DepotItemImportLineMapper;
import io.github.matwein.xmc.be.services.importing.controller.ImportPreparationController;
import io.github.matwein.xmc.common.services.depot.IDepotItemImportService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemImportColmn;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepotItemImportService implements IDepotItemImportService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotItemImportService.class);
	
	private final ImportPreparationController importPreparationController;
	private final DepotDeliveryJpaRepository depotDeliveryJpaRepository;
	private final DepotItemImportLineMapper depotItemImportLineMapper;
	private final DepotItemImportLineValidator depotItemImportLineValidator;
	private final DepotItemImportController depotItemImportController;
	
	@Autowired
	public DepotItemImportService(
			ImportPreparationController importPreparationController,
			DepotDeliveryJpaRepository depotDeliveryJpaRepository,
			DepotItemImportLineMapper depotItemImportLineMapper,
			DepotItemImportLineValidator depotItemImportLineValidator,
			DepotItemImportController depotItemImportController) {
		
		this.importPreparationController = importPreparationController;
		this.depotDeliveryJpaRepository = depotDeliveryJpaRepository;
		this.depotItemImportLineMapper = depotItemImportLineMapper;
		this.depotItemImportLineValidator = depotItemImportLineValidator;
		this.depotItemImportController = depotItemImportController;
	}
	
	@Override
	public DtoImportFileValidationResult<DtoDepotItemImportRow> readAndValidateImportFile(
			IAsyncMonitor monitor,
			DtoImportData<DepotItemImportColmn> importData) {
		
		LOGGER.info("Validating depot item import file: {}", importData);
		return importPreparationController.readAndValidateImportFile(
				monitor,
				importData,
				depotItemImportLineMapper,
				depotItemImportLineValidator);
	}
	
	@Override
	public void importDeliveries(
			IAsyncMonitor monitor,
			long depotDeliveryId,
			DtoImportData<DepotItemImportColmn> importData) {
		
		LOGGER.info("Importing depot item import file: {}", importData);
		DepotDelivery depotDelivery = depotDeliveryJpaRepository.getReferenceById(depotDeliveryId);
		
		depotItemImportController.importDepotItems(monitor, depotDelivery, importData);
	}
}
