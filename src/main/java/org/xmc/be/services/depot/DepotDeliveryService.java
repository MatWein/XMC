package org.xmc.be.services.depot;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import org.xmc.be.repositories.depot.DepotDeliveryRepository;
import org.xmc.be.repositories.depot.DepotJpaRepository;
import org.xmc.be.services.depot.controller.DepotDeliverySaveController;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDelivery;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryOverview;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDateTime;

@Service
@Transactional
public class DepotDeliveryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotDeliveryService.class);
	
	private final DepotJpaRepository depotJpaRepository;
	private final DepotDeliveryJpaRepository depotDeliveryJpaRepository;
	private final DepotDeliveryRepository depotDeliveryRepository;
	private final DepotDeliverySaveController depotDeliverySaveController;
	
	@Autowired
	public DepotDeliveryService(
			DepotJpaRepository depotJpaRepository,
			DepotDeliveryJpaRepository depotDeliveryJpaRepository,
			DepotDeliveryRepository depotDeliveryRepository,
			DepotDeliverySaveController depotDeliverySaveController) {
		
		this.depotJpaRepository = depotJpaRepository;
		this.depotDeliveryJpaRepository = depotDeliveryJpaRepository;
		this.depotDeliveryRepository = depotDeliveryRepository;
		this.depotDeliverySaveController = depotDeliverySaveController;
	}
	
	public QueryResults<DtoDepotDeliveryOverview> loadOverview(
			AsyncMonitor monitor,
			long depotId,
			PagingParams<DepotDeliveryOverviewFields> pagingParams) {
		
		LOGGER.info("Loading depot delivery overview: {}", pagingParams);
		monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_DEPOT_DELIVERY_OVERVIEW);
		
		Depot depot = depotJpaRepository.getOne(depotId);
		return depotDeliveryRepository.loadOverview(depot, pagingParams);
	}
	
	public void saveOrUpdate(AsyncMonitor monitor, long depotId, DtoDepotDelivery dtoDepotDelivery) {
		LOGGER.info("Saving depot delivery: {}", dtoDepotDelivery);
		monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_DEPOT_DELIVERY);
		
		Depot depot = depotJpaRepository.getOne(depotId);
		depotDeliverySaveController.saveOrUpdate(depot, dtoDepotDelivery);
	}
	
	public void markAsDeleted(AsyncMonitor monitor, long deliveryId) {
		LOGGER.info("Marking depot delivery '{}' as deleted.", deliveryId);
		monitor.setStatusText(MessageKey.ASYNC_TASK_DELETE_DEPOT_DELIVERY);
		
		DepotDelivery depotDelivery = depotDeliveryJpaRepository.getOne(deliveryId);
		depotDelivery.setDeletionDate(LocalDateTime.now());
		depotDeliveryJpaRepository.save(depotDelivery);
	}
}
