package io.github.matwein.xmc.be.services.depot;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.repositories.depot.DepotJpaRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotRepository;
import io.github.matwein.xmc.be.services.depot.controller.DepotSaveController;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.depot.DepotOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.DtoDepot;
import io.github.matwein.xmc.common.stubs.depot.DtoDepotOverview;
import io.github.matwein.xmc.fe.async.AsyncMonitor;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDateTime;

@Service
@Transactional
public class DepotService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepotService.class);
	
	private final DepotRepository depotRepository;
	private final DepotJpaRepository depotJpaRepository;
	private final DepotSaveController depotSaveController;
	
	@Autowired
	public DepotService(
			DepotRepository depotRepository,
			DepotJpaRepository depotJpaRepository,
			DepotSaveController depotSaveController) {
		
		this.depotRepository = depotRepository;
		this.depotJpaRepository = depotJpaRepository;
		this.depotSaveController = depotSaveController;
	}
	
	public QueryResults<DtoDepotOverview> loadOverview(AsyncMonitor monitor, PagingParams<DepotOverviewFields> pagingParams) {
		LOGGER.info("Loading depot overview: {}", pagingParams);
		monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_DEPOT_OVERVIEW);
		
		return depotRepository.loadOverview(pagingParams);
	}
	
	public void saveOrUpdate(AsyncMonitor monitor, DtoDepot dtoDepot) {
		LOGGER.info("Saving depot: {}", dtoDepot);
		monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_DEPOT);
		
		depotSaveController.saveOrUpdate(dtoDepot);
	}
	
	public void markAsDeleted(AsyncMonitor monitor, long depotId) {
		LOGGER.info("Marking depot '{}' as deleted.", depotId);
		monitor.setStatusText(MessageKey.ASYNC_TASK_DELETE_DEPOT);
		
		Depot depot = depotJpaRepository.getOne(depotId);
		depot.setDeletionDate(LocalDateTime.now());
		depotJpaRepository.save(depot);
	}
}
