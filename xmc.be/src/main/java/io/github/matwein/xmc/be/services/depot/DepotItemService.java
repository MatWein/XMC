package io.github.matwein.xmc.be.services.depot;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.common.mapper.QueryResultsMapper;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.be.entities.depot.DepotItem;
import io.github.matwein.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotItemJpaRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotItemRepository;
import io.github.matwein.xmc.be.services.depot.controller.DeliverySaldoUpdatingController;
import io.github.matwein.xmc.be.services.depot.controller.DepotItemSaveController;
import io.github.matwein.xmc.common.services.depot.IDepotItemService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItem;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemOverview;
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
public class DepotItemService implements IDepotItemService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotItemService.class);
	
	private final DepotDeliveryJpaRepository depotDeliveryJpaRepository;
	private final DepotItemRepository depotItemRepository;
	private final DepotItemSaveController depotItemSaveController;
	private final DeliverySaldoUpdatingController deliverySaldoUpdatingController;
	private final DepotItemJpaRepository depotItemJpaRepository;
	private final QueryResultsMapper queryResultsMapper;
	
	@Autowired
	public DepotItemService(
			DepotDeliveryJpaRepository depotDeliveryJpaRepository,
			DepotItemRepository depotItemRepository,
			DepotItemSaveController depotItemSaveController,
			DeliverySaldoUpdatingController deliverySaldoUpdatingController,
			DepotItemJpaRepository depotItemJpaRepository,
			QueryResultsMapper queryResultsMapper) {
		
		this.depotDeliveryJpaRepository = depotDeliveryJpaRepository;
		this.depotItemRepository = depotItemRepository;
		this.depotItemSaveController = depotItemSaveController;
		this.deliverySaldoUpdatingController = deliverySaldoUpdatingController;
		this.depotItemJpaRepository = depotItemJpaRepository;
		this.queryResultsMapper = queryResultsMapper;
	}
	
	@Override
	public QueryResults<DtoDepotItemOverview> loadOverview(
			IAsyncMonitor monitor,
			long depotDeliveryId,
			PagingParams<DepotItemOverviewFields> pagingParams) {
		
		LOGGER.info("Loading depot item overview for delivery '{}': {}", depotDeliveryId, pagingParams);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_DEPOT_ITEM_OVERVIEW));
		
		DepotDelivery depotDelivery = depotDeliveryJpaRepository.getOne(depotDeliveryId);
		var results = depotItemRepository.loadOverview(depotDelivery, pagingParams);
		return queryResultsMapper.map(results);
	}
	
	@Override
	public void saveOrUpdate(IAsyncMonitor monitor, long depotDeliveryId, DtoDepotItem dtoDepotItem) {
		LOGGER.info("Saving depot item: {}", dtoDepotItem);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_DEPOT_ITEM));
		
		DepotDelivery depotDelivery = depotDeliveryJpaRepository.getOne(depotDeliveryId);
		depotItemSaveController.saveOrUpdate(depotDelivery, dtoDepotItem);
		
		deliverySaldoUpdatingController.updateDeliverySaldo(depotDelivery);
	}
	
	@Override
	public void markAsDeleted(IAsyncMonitor monitor, long depotDeliveryId, Set<Long> depotItemIds) {
		LOGGER.info("Marking depot items '{}' as deleted.", depotItemIds);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_DELETE_DEPOT_ITEMS));
		
		List<DepotItem> items = depotItemJpaRepository.findAllById(depotItemIds);
		
		for (DepotItem item : items) {
			item.setDeletionDate(LocalDateTime.now());
			depotItemJpaRepository.save(item);
		}
		
		DepotDelivery depotDelivery = depotDeliveryJpaRepository.getOne(depotDeliveryId);
		deliverySaldoUpdatingController.updateDeliverySaldo(depotDelivery);
	}
}
