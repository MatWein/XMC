package io.github.matwein.xmc.be.services.depot;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.common.mapper.QueryResultsMapper;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotDeliveryRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotJpaRepository;
import io.github.matwein.xmc.be.services.depot.controller.DepotDeliverySaveController;
import io.github.matwein.xmc.be.services.depot.controller.LastDeliveryUpdatingController;
import io.github.matwein.xmc.common.services.depot.IDepotDeliveryService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDelivery;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryOverview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class DepotDeliveryService implements IDepotDeliveryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotDeliveryService.class);
	
	private final DepotJpaRepository depotJpaRepository;
	private final DepotDeliveryJpaRepository depotDeliveryJpaRepository;
	private final DepotDeliveryRepository depotDeliveryRepository;
	private final DepotDeliverySaveController depotDeliverySaveController;
	private final LastDeliveryUpdatingController lastDeliveryUpdatingController;
	private final QueryResultsMapper queryResultsMapper;
	
	@Autowired
	public DepotDeliveryService(
			DepotJpaRepository depotJpaRepository,
			DepotDeliveryJpaRepository depotDeliveryJpaRepository,
			DepotDeliveryRepository depotDeliveryRepository,
			DepotDeliverySaveController depotDeliverySaveController,
			LastDeliveryUpdatingController lastDeliveryUpdatingController,
			QueryResultsMapper queryResultsMapper) {
		
		this.depotJpaRepository = depotJpaRepository;
		this.depotDeliveryJpaRepository = depotDeliveryJpaRepository;
		this.depotDeliveryRepository = depotDeliveryRepository;
		this.depotDeliverySaveController = depotDeliverySaveController;
		this.lastDeliveryUpdatingController = lastDeliveryUpdatingController;
		this.queryResultsMapper = queryResultsMapper;
	}
	
	@Override
	public QueryResults<DtoDepotDeliveryOverview> loadOverview(
			IAsyncMonitor monitor,
			long depotId,
			PagingParams<DepotDeliveryOverviewFields> pagingParams) {
		
		LOGGER.info("Loading depot delivery overview: {}", pagingParams);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_DEPOT_DELIVERY_OVERVIEW));
		
		Depot depot = depotJpaRepository.getById(depotId);
		var results = depotDeliveryRepository.loadOverview(depot, pagingParams);
		return queryResultsMapper.map(results);
	}
	
	@Override
	public void saveOrUpdate(IAsyncMonitor monitor, long depotId, DtoDepotDelivery dtoDepotDelivery) {
		LOGGER.info("Saving depot delivery: {}", dtoDepotDelivery);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_DEPOT_DELIVERY));
		
		Depot depot = depotJpaRepository.getById(depotId);
		depotDeliverySaveController.saveOrUpdate(depot, dtoDepotDelivery);
		
		lastDeliveryUpdatingController.updateLastDeliveryOfDepot(depot);
	}
	
	@Override
	public void markAsDeleted(IAsyncMonitor monitor, long deliveryId) {
		LOGGER.info("Marking depot delivery '{}' as deleted.", deliveryId);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_DELETE_DEPOT_DELIVERY));
		
		DepotDelivery depotDelivery = depotDeliveryJpaRepository.getById(deliveryId);
		depotDelivery.setDeletionDate(LocalDateTime.now());
		depotDeliveryJpaRepository.save(depotDelivery);
		
		lastDeliveryUpdatingController.updateLastDeliveryOfDepot(depotDelivery.getDepot());
	}
}
