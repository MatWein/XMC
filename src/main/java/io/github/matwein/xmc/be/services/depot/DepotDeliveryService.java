package io.github.matwein.xmc.be.services.depot;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotDeliveryRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotJpaRepository;
import io.github.matwein.xmc.be.services.depot.controller.DepotDeliverySaveController;
import io.github.matwein.xmc.be.services.depot.controller.LastDeliveryUpdatingController;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDelivery;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryOverview;
import io.github.matwein.xmc.fe.async.AsyncMonitor;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDateTime;

@Service
@Transactional
public class DepotDeliveryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotDeliveryService.class);
	
	private final DepotJpaRepository depotJpaRepository;
	private final DepotDeliveryJpaRepository depotDeliveryJpaRepository;
	private final DepotDeliveryRepository depotDeliveryRepository;
	private final DepotDeliverySaveController depotDeliverySaveController;
	private final LastDeliveryUpdatingController lastDeliveryUpdatingController;
	
	@Autowired
	public DepotDeliveryService(
			DepotJpaRepository depotJpaRepository,
			DepotDeliveryJpaRepository depotDeliveryJpaRepository,
			DepotDeliveryRepository depotDeliveryRepository,
			DepotDeliverySaveController depotDeliverySaveController,
			LastDeliveryUpdatingController lastDeliveryUpdatingController) {
		
		this.depotJpaRepository = depotJpaRepository;
		this.depotDeliveryJpaRepository = depotDeliveryJpaRepository;
		this.depotDeliveryRepository = depotDeliveryRepository;
		this.depotDeliverySaveController = depotDeliverySaveController;
		this.lastDeliveryUpdatingController = lastDeliveryUpdatingController;
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
		
		lastDeliveryUpdatingController.updateLastDeliveryOfDepot(depot);
	}
	
	public void markAsDeleted(AsyncMonitor monitor, long deliveryId) {
		LOGGER.info("Marking depot delivery '{}' as deleted.", deliveryId);
		monitor.setStatusText(MessageKey.ASYNC_TASK_DELETE_DEPOT_DELIVERY);
		
		DepotDelivery depotDelivery = depotDeliveryJpaRepository.getOne(deliveryId);
		depotDelivery.setDeletionDate(LocalDateTime.now());
		depotDeliveryJpaRepository.save(depotDelivery);
		
		lastDeliveryUpdatingController.updateLastDeliveryOfDepot(depotDelivery.getDepot());
	}
}
