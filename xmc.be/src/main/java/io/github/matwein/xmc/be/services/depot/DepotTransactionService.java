package io.github.matwein.xmc.be.services.depot;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.common.mapper.QueryResultsMapper;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotTransaction;
import io.github.matwein.xmc.be.repositories.depot.DepotJpaRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotTransactionJpaRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotTransactionRepository;
import io.github.matwein.xmc.be.services.depot.controller.DepotTransactionSaveController;
import io.github.matwein.xmc.common.services.depot.IDepotTransactionService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransaction;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransactionOverview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class DepotTransactionService implements IDepotTransactionService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotTransactionService.class);
	
	private final DepotTransactionJpaRepository depotTransactionJpaRepository;
	private final DepotJpaRepository depotJpaRepository;
	private final DepotTransactionRepository depotTransactionRepository;
	private final DepotTransactionSaveController depotTransactionSaveController;
	private final QueryResultsMapper queryResultsMapper;
	
	@Autowired
	public DepotTransactionService(
			DepotTransactionJpaRepository depotTransactionJpaRepository,
			DepotJpaRepository depotJpaRepository,
			DepotTransactionRepository depotTransactionRepository,
			DepotTransactionSaveController depotTransactionSaveController,
			QueryResultsMapper queryResultsMapper) {
		
		this.depotTransactionJpaRepository = depotTransactionJpaRepository;
		this.depotJpaRepository = depotJpaRepository;
		this.depotTransactionRepository = depotTransactionRepository;
		this.depotTransactionSaveController = depotTransactionSaveController;
		this.queryResultsMapper = queryResultsMapper;
	}
	
	@Override
	public QueryResults<DtoDepotTransactionOverview> loadOverview(
			IAsyncMonitor monitor,
			long depotId,
			PagingParams<DepotTransactionOverviewFields> pagingParams) {
		
		LOGGER.info("Loading depot transaction overview: {}", pagingParams);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_DEPOT_TRANSACTION_OVERVIEW));
		
		Depot depot = depotJpaRepository.getOne(depotId);
		var results = depotTransactionRepository.loadOverview(depot, pagingParams);
		return queryResultsMapper.map(results);
	}
	
	@Override
	public void saveOrUpdate(IAsyncMonitor monitor, long depotId, DtoDepotTransaction dtoDepotTransaction) {
		LOGGER.info("Saving depot transaction: {}", dtoDepotTransaction);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_DEPOT_TRANSACTION));
		
		Depot depot = depotJpaRepository.getOne(depotId);
		depotTransactionSaveController.saveOrUpdate(depot, dtoDepotTransaction);
	}
	
	@Override
	public void markAsDeleted(IAsyncMonitor monitor, Set<Long> transactionIds) {
		LOGGER.info("Marking depot transactions '{}' as deleted.", transactionIds);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_DELETE_DEPOT_TRANSACTIONS));
		
		List<DepotTransaction> transactions = depotTransactionJpaRepository.findAllById(transactionIds);
		
		for (DepotTransaction transaction : transactions) {
			transaction.setDeletionDate(LocalDateTime.now());
			depotTransactionJpaRepository.save(transaction);
		}
	}
}
