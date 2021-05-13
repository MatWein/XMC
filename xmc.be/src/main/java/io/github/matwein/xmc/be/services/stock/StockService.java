package io.github.matwein.xmc.be.services.stock;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.common.mapper.QueryResultsMapper;
import io.github.matwein.xmc.be.entities.depot.Stock;
import io.github.matwein.xmc.be.repositories.stock.StockJpaRepository;
import io.github.matwein.xmc.be.repositories.stock.StockRepository;
import io.github.matwein.xmc.be.services.stock.controller.StockSaveController;
import io.github.matwein.xmc.common.services.stock.IStockService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.stocks.DtoMinimalStock;
import io.github.matwein.xmc.common.stubs.stocks.DtoStock;
import io.github.matwein.xmc.common.stubs.stocks.DtoStockOverview;
import io.github.matwein.xmc.common.stubs.stocks.StockOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockService implements IStockService {
	private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);
	
	private final StockJpaRepository stockJpaRepository;
	private final StockRepository stockRepository;
	private final StockSaveController stockSaveController;
	private final QueryResultsMapper queryResultsMapper;
	
	@Autowired
	public StockService(
			StockJpaRepository stockJpaRepository,
			StockRepository stockRepository,
			StockSaveController stockSaveController,
			QueryResultsMapper queryResultsMapper) {
		
		this.stockJpaRepository = stockJpaRepository;
		this.stockRepository = stockRepository;
		this.stockSaveController = stockSaveController;
		this.queryResultsMapper = queryResultsMapper;
	}
	
	@Override
	public QueryResults<DtoStockOverview> loadOverview(IAsyncMonitor monitor, PagingParams<StockOverviewFields> pagingParams) {
		LOGGER.info("Loading stock overview: {}", pagingParams);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_STOCK_OVERVIEW));
		
		var results = stockRepository.loadOverview(pagingParams);
		return queryResultsMapper.map(results);
	}
	
	@Override
	public void saveOrUpdate(IAsyncMonitor monitor, DtoStock dtoStock) {
		LOGGER.info("Saving stock: {}", dtoStock);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_STOCK));
		
		stockSaveController.saveOrUpdate(dtoStock);
	}
	
	@Override
	public void deleteStock(IAsyncMonitor monitor, long stockId) {
		LOGGER.info("Deleting stock with id '{}'.", stockId);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_DELETE_STOCK));
		
		stockJpaRepository.deleteById(stockId);
	}
	
	@Override
	public List<String> loadAllStockIsins() {
		LOGGER.info("Loading all stock isins.");
		
		return stockJpaRepository.findAll().stream()
				.map(Stock::getIsin)
				.map(String::toUpperCase)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<DtoMinimalStock> loadAllStocks(String searchValue, int limit) {
		LOGGER.info("Loading all stock information. searchValue: {}, limit: {}", searchValue, limit);
		return stockRepository.loadAllStocks(searchValue, limit);
	}
}
