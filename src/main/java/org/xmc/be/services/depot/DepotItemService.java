package org.xmc.be.services.depot;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.be.entities.depot.DepotItem;
import org.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import org.xmc.be.repositories.depot.DepotItemJpaRepository;
import org.xmc.be.repositories.depot.DepotItemRepository;
import org.xmc.be.services.depot.controller.DeliverySaldoUpdatingController;
import org.xmc.be.services.depot.controller.DepotItemSaveController;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.depot.deliveries.DepotItemOverviewFields;
import org.xmc.common.stubs.depot.deliveries.DtoDepotItem;
import org.xmc.common.stubs.depot.deliveries.DtoDepotItemOverview;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class DepotItemService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotItemService.class);
	
	private final DepotDeliveryJpaRepository depotDeliveryJpaRepository;
	private final DepotItemRepository depotItemRepository;
	private final DepotItemSaveController depotItemSaveController;
	private final DeliverySaldoUpdatingController deliverySaldoUpdatingController;
	private final DepotItemJpaRepository depotItemJpaRepository;
	
	@Autowired
	public DepotItemService(
			DepotDeliveryJpaRepository depotDeliveryJpaRepository,
			DepotItemRepository depotItemRepository,
			DepotItemSaveController depotItemSaveController,
			DeliverySaldoUpdatingController deliverySaldoUpdatingController,
			DepotItemJpaRepository depotItemJpaRepository) {
		
		this.depotDeliveryJpaRepository = depotDeliveryJpaRepository;
		this.depotItemRepository = depotItemRepository;
		this.depotItemSaveController = depotItemSaveController;
		this.deliverySaldoUpdatingController = deliverySaldoUpdatingController;
		this.depotItemJpaRepository = depotItemJpaRepository;
	}
	
	public QueryResults<DtoDepotItemOverview> loadOverview(
			AsyncMonitor monitor,
			long depotDeliveryId,
			PagingParams<DepotItemOverviewFields> pagingParams) {
		
		LOGGER.info("Loading depot item overview for delivery '{}': {}", depotDeliveryId, pagingParams);
		monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_DEPOT_ITEM_OVERVIEW);
		
		DepotDelivery depotDelivery = depotDeliveryJpaRepository.getOne(depotDeliveryId);
		return depotItemRepository.loadOverview(depotDelivery, pagingParams);
	}
	
	public void saveOrUpdate(AsyncMonitor monitor, long depotDeliveryId, DtoDepotItem dtoDepotItem) {
		LOGGER.info("Saving depot item: {}", dtoDepotItem);
		monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_DEPOT_ITEM);
		
		DepotDelivery depotDelivery = depotDeliveryJpaRepository.getOne(depotDeliveryId);
		depotItemSaveController.saveOrUpdate(depotDelivery, dtoDepotItem);
		
		deliverySaldoUpdatingController.updateDeliverySaldo(depotDelivery);
	}
	
	public void markAsDeleted(AsyncMonitor monitor, long depotDeliveryId, Set<Long> depotItemIds) {
		LOGGER.info("Marking depot items '{}' as deleted.", depotItemIds);
		monitor.setStatusText(MessageKey.ASYNC_TASK_DELETE_DEPOT_ITEMS);
		
		List<DepotItem> items = depotItemJpaRepository.findAllById(depotItemIds);
		
		for (DepotItem item : items) {
			item.setDeletionDate(LocalDateTime.now());
			depotItemJpaRepository.save(item);
		}
		
		DepotDelivery depotDelivery = depotDeliveryJpaRepository.getOne(depotDeliveryId);
		deliverySaldoUpdatingController.updateDeliverySaldo(depotDelivery);
	}
}
