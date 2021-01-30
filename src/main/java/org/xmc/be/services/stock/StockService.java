package org.xmc.be.services.stock;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.depot.Stock;
import org.xmc.be.repositories.stock.StockJpaRepository;
import org.xmc.be.repositories.stock.StockRepository;
import org.xmc.be.services.stock.controller.StockSaveController;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.stocks.DtoMinimalStock;
import org.xmc.common.stubs.stocks.DtoStock;
import org.xmc.common.stubs.stocks.DtoStockOverview;
import org.xmc.common.stubs.stocks.StockOverviewFields;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockService {
	private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);
	
	private final StockJpaRepository stockJpaRepository;
	private final StockRepository stockRepository;
	private final StockSaveController stockSaveController;
	
	@Autowired
	public StockService(
			StockJpaRepository stockJpaRepository,
			StockRepository stockRepository,
			StockSaveController stockSaveController) {
		
		this.stockJpaRepository = stockJpaRepository;
		this.stockRepository = stockRepository;
		this.stockSaveController = stockSaveController;
	}
	
	public QueryResults<DtoStockOverview> loadOverview(AsyncMonitor monitor, PagingParams<StockOverviewFields> pagingParams) {
		LOGGER.info("Loading stock overview: {}", pagingParams);
		monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_STOCK_OVERVIEW);
		
		return stockRepository.loadOverview(pagingParams);
	}
	
	public void saveOrUpdate(AsyncMonitor monitor, DtoStock dtoStock) {
		LOGGER.info("Saving stock: {}", dtoStock);
		monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_STOCK);
		
		stockSaveController.saveOrUpdate(dtoStock);
	}
	
	public void deleteStock(AsyncMonitor monitor, long stockId) {
		monitor.setStatusText(MessageKey.ASYNC_TASK_DELETE_STOCK);
		
		Optional<Stock> existingStock = stockJpaRepository.findById(stockId);
		if (existingStock.isPresent()) {
			LOGGER.info("Deleting stock '{}'.", existingStock.get());
			stockJpaRepository.delete(existingStock.get());
		}
	}
	
	public List<String> loadAllStockIsins() {
		LOGGER.info("Loading all stock isins.");
		
		return stockJpaRepository.findAll().stream()
				.map(Stock::getIsin)
				.map(String::toUpperCase)
				.collect(Collectors.toList());
	}
	
	public List<DtoMinimalStock> loadAllStocks(String searchValue, int limit) {
		LOGGER.info("Loading all stock information. searchValue: {}, limit: {}", searchValue, limit);
		return stockRepository.loadAllStocks(searchValue, limit);
	}
}
