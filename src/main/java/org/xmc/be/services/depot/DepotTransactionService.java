package org.xmc.be.services.depot;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotTransaction;
import org.xmc.be.repositories.depot.DepotJpaRepository;
import org.xmc.be.repositories.depot.DepotTransactionJpaRepository;
import org.xmc.be.repositories.depot.DepotTransactionRepository;
import org.xmc.be.services.depot.controller.DepotTransactionSaveController;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransaction;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransactionOverview;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class DepotTransactionService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotTransactionService.class);
	
	private final DepotTransactionJpaRepository depotTransactionJpaRepository;
	private final DepotJpaRepository depotJpaRepository;
	private final DepotTransactionRepository depotTransactionRepository;
	private final DepotTransactionSaveController depotTransactionSaveController;
	
	@Autowired
	public DepotTransactionService(
			DepotTransactionJpaRepository depotTransactionJpaRepository,
			DepotJpaRepository depotJpaRepository,
			DepotTransactionRepository depotTransactionRepository,
			DepotTransactionSaveController depotTransactionSaveController) {
		
		this.depotTransactionJpaRepository = depotTransactionJpaRepository;
		this.depotJpaRepository = depotJpaRepository;
		this.depotTransactionRepository = depotTransactionRepository;
		this.depotTransactionSaveController = depotTransactionSaveController;
	}
	
	public QueryResults<DtoDepotTransactionOverview> loadOverview(
			AsyncMonitor monitor,
			long depotId,
			PagingParams<DepotTransactionOverviewFields> pagingParams) {
		
		LOGGER.info("Loading depot transaction overview: {}", pagingParams);
		monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_DEPOT_TRANSACTION_OVERVIEW);
		
		Depot depot = depotJpaRepository.getOne(depotId);
		return depotTransactionRepository.loadOverview(depot, pagingParams);
	}
	
	public void saveOrUpdate(AsyncMonitor monitor, long depotId, DtoDepotTransaction dtoDepotTransaction) {
		LOGGER.info("Saving depot transaction: {}", dtoDepotTransaction);
		monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_DEPOT_TRANSACTION);
		
		Depot depot = depotJpaRepository.getOne(depotId);
		depotTransactionSaveController.saveOrUpdate(depot, dtoDepotTransaction);
	}
	
	public void markAsDeleted(AsyncMonitor monitor, Set<Long> transactionIds) {
		LOGGER.info("Marking depot transactions '{}' as deleted.", transactionIds);
		monitor.setStatusText(MessageKey.ASYNC_TASK_DELETE_DEPOT_TRANSACTIONS);
		
		List<DepotTransaction> transactions = depotTransactionJpaRepository.findAllById(transactionIds);
		
		for (DepotTransaction transaction : transactions) {
			transaction.setDeletionDate(LocalDateTime.now());
			depotTransactionJpaRepository.save(transaction);
		}
	}
}
