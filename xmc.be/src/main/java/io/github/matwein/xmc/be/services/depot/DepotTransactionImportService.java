package io.github.matwein.xmc.be.services.depot;

import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.repositories.depot.DepotJpaRepository;
import io.github.matwein.xmc.be.services.depot.controller.importing.DepotTransactionImportController;
import io.github.matwein.xmc.be.services.depot.controller.importing.DepotTransactionImportLineValidator;
import io.github.matwein.xmc.be.services.depot.mapper.DepotTransactionImportLineMapper;
import io.github.matwein.xmc.be.services.importing.controller.ImportPreparationController;
import io.github.matwein.xmc.common.services.depot.IDepotTransactionImportService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransactionImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepotTransactionImportService implements IDepotTransactionImportService {
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
	
	@Override
	public DtoImportFileValidationResult<DtoDepotTransactionImportRow> readAndValidateImportFile(
			IAsyncMonitor monitor,
			DtoImportData<DepotTransactionImportColmn> importData) {
		
		LOGGER.info("Validating depot transaction import file: {}", importData);
		return importPreparationController.readAndValidateImportFile(
				monitor,
				importData,
				depotTransactionImportLineMapper,
				depotTransactionImportLineValidator);
	}
	
	@Override
	public void importTransactions(IAsyncMonitor monitor, long depotId, DtoImportData<DepotTransactionImportColmn> importData) {
		LOGGER.info("Importing depot transaction import file: {}", importData);
		Depot depot = depotJpaRepository.getById(depotId);
		
		depotTransactionImportController.importDepotTransactions(monitor, depot, importData);
	}
}
