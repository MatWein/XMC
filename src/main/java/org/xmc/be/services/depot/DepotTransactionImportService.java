package org.xmc.be.services.depot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.repositories.depot.DepotJpaRepository;
import org.xmc.be.services.depot.controller.importing.DepotTransactionImportController;
import org.xmc.be.services.depot.controller.importing.DepotTransactionImportLineValidator;
import org.xmc.be.services.depot.mapper.DepotTransactionImportLineMapper;
import org.xmc.be.services.importing.controller.ImportPreparationController;
import org.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransactionImportRow;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.fe.async.AsyncMonitor;

@Service
@Transactional
public class DepotTransactionImportService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotTransactionImportService.class);
	
	private final ImportPreparationController importPreparationController;
	private final DepotJpaRepository depotJpaRepository;
	private final DepotTransactionImportLineMapper depotTransactionImportLineMapper;
	private final DepotTransactionImportLineValidator depotTransactionImportLineValidator;
	private final DepotTransactionImportController depotTransactionImportController;
	
	@Autowired
	public DepotTransactionImportService(
			ImportPreparationController importPreparationController,
			DepotJpaRepository depotJpaRepository,
			DepotTransactionImportLineMapper depotTransactionImportLineMapper,
			DepotTransactionImportLineValidator depotTransactionImportLineValidator,
			DepotTransactionImportController depotTransactionImportController) {
		
		this.importPreparationController = importPreparationController;
		this.depotJpaRepository = depotJpaRepository;
		this.depotTransactionImportLineMapper = depotTransactionImportLineMapper;
		this.depotTransactionImportLineValidator = depotTransactionImportLineValidator;
		this.depotTransactionImportController = depotTransactionImportController;
	}
	
	public DtoImportFileValidationResult<DtoDepotTransactionImportRow> readAndValidateImportFile(
			AsyncMonitor monitor,
			DtoImportData<DepotTransactionImportColmn> importData) {
		
		LOGGER.info("Validating depot transaction import file: {}", importData);
		return importPreparationController.readAndValidateImportFile(
				monitor,
				importData,
				depotTransactionImportLineMapper,
				depotTransactionImportLineValidator);
	}
	
	public void importTransactions(AsyncMonitor monitor, long depotId, DtoImportData<DepotTransactionImportColmn> importData) {
		LOGGER.info("Importing depot transaction import file: {}", importData);
		Depot depot = depotJpaRepository.getOne(depotId);
		
		depotTransactionImportController.importDepotTransactions(monitor, depot, importData);
	}
}
