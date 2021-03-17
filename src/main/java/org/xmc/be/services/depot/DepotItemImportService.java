package org.xmc.be.services.depot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import org.xmc.be.services.depot.controller.importing.DepotItemImportController;
import org.xmc.be.services.depot.controller.importing.DepotItemImportLineValidator;
import org.xmc.be.services.depot.mapper.DepotItemImportLineMapper;
import org.xmc.be.services.importing.controller.ImportPreparationController;
import org.xmc.common.stubs.depot.items.DepotItemImportColmn;
import org.xmc.common.stubs.depot.items.DtoDepotItemImportRow;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.fe.async.AsyncMonitor;

@Service
@Transactional
public class DepotItemImportService {
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
	
	public DtoImportFileValidationResult<DtoDepotItemImportRow> readAndValidateImportFile(
			AsyncMonitor monitor,
			DtoImportData<DepotItemImportColmn> importData) {
		
		LOGGER.info("Validating depot item import file: {}", importData);
		return importPreparationController.readAndValidateImportFile(
				monitor,
				importData,
				depotItemImportLineMapper,
				depotItemImportLineValidator);
	}
	
	public void importDeliveries(
			AsyncMonitor monitor,
			long depotDeliveryId,
			DtoImportData<DepotItemImportColmn> importData) {
		
		LOGGER.info("Importing depot item import file: {}", importData);
		DepotDelivery depotDelivery = depotDeliveryJpaRepository.getOne(depotDeliveryId);
		
		depotItemImportController.importDepotItems(monitor, depotDelivery, importData);
	}
}
