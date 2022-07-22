package io.github.matwein.xmc.be.services.category;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.common.mapper.QueryResultsMapper;
import io.github.matwein.xmc.be.entities.depot.StockCategory;
import io.github.matwein.xmc.be.repositories.category.StockCategoryJpaRepository;
import io.github.matwein.xmc.be.repositories.category.StockCategoryRepository;
import io.github.matwein.xmc.be.services.category.controller.StockCategorySaveController;
import io.github.matwein.xmc.common.services.category.IStockCategoryService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.category.DtoStockCategory;
import io.github.matwein.xmc.common.stubs.category.DtoStockCategoryOverview;
import io.github.matwein.xmc.common.stubs.category.StockCategoryOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockCategoryService implements IStockCategoryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(StockCategoryService.class);
	
	private static final PagingParams<StockCategoryOverviewFields> PAGING_PARAMS_ALL_CATEGORIES = new PagingParams<>(0, Integer.MAX_VALUE, StockCategoryOverviewFields.NAME, Order.ASC, null);
	
	private final StockCategoryJpaRepository stockCategoryJpaRepository;
	private final StockCategoryRepository stockCategoryRepository;
	private final StockCategorySaveController stockCategorySaveController;
	private final QueryResultsMapper queryResultsMapper;
	
	@Autowired
	public StockCategoryService(
			StockCategoryJpaRepository stockCategoryJpaRepository,
			StockCategoryRepository stockCategoryRepository,
			StockCategorySaveController stockCategorySaveController,
			QueryResultsMapper queryResultsMapper) {
		
		this.stockCategoryJpaRepository = stockCategoryJpaRepository;
		this.stockCategoryRepository = stockCategoryRepository;
		this.stockCategorySaveController = stockCategorySaveController;
		this.queryResultsMapper = queryResultsMapper;
	}
	
	@Override
	public void saveOrUpdate(IAsyncMonitor monitor, DtoStockCategory dtoCategory) {
        LOGGER.info("Saving stock category: {}", dtoCategory);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_CATEGORY));

        stockCategorySaveController.saveOrUpdate(dtoCategory);
    }
	
	@Override
    public QueryResults<DtoStockCategoryOverview> loadOverview(IAsyncMonitor monitor, PagingParams<StockCategoryOverviewFields> pagingParams) {
        LOGGER.info("Loading stock category overview: {}", pagingParams);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_CATEGORY_OVERVIEW));
		
		var results = stockCategoryRepository.loadOverview(pagingParams);
		return queryResultsMapper.map(results);
    }
	
	@Override
    public void markAsDeleted(IAsyncMonitor monitor, Long categoryId) {
        LOGGER.info("Marking stock category '{}' as deleted.", categoryId);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_DELETE_CATEGORY));

        StockCategory category = stockCategoryJpaRepository.getReferenceById(categoryId);
        category.setDeletionDate(LocalDateTime.now());
	    stockCategoryJpaRepository.save(category);
    }
	
	@Override
	public List<DtoStockCategoryOverview> loadAllStockCategories(IAsyncMonitor monitor) {
		LOGGER.info("Loading all stock categories.");
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_ALL_STOCK_CATEGORIES));
		
		return stockCategoryRepository.loadOverview(PAGING_PARAMS_ALL_CATEGORIES).getResults();
	}
	
	@Override
	public List<String> loadAllStockCategoryNames() {
		return stockCategoryRepository.loadOverview(PAGING_PARAMS_ALL_CATEGORIES).getResults().stream()
				.map(DtoStockCategoryOverview::getName)
				.collect(Collectors.toList());
	}
}
