package io.github.matwein.xmc.be.services.category;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.be.entities.depot.StockCategory;
import io.github.matwein.xmc.be.repositories.category.StockCategoryJpaRepository;
import io.github.matwein.xmc.be.repositories.category.StockCategoryRepository;
import io.github.matwein.xmc.be.services.category.controller.StockCategorySaveController;
import io.github.matwein.xmc.be.services.category.mapper.StockCateogoryToDtoStockCategoryMapper;
import io.github.matwein.xmc.common.services.category.IStockCategoryService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.category.DtoStockCategory;
import io.github.matwein.xmc.common.stubs.category.DtoStockCategoryOverview;
import io.github.matwein.xmc.common.stubs.category.StockCategoryOverviewFields;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
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
	
    private final StockCategoryJpaRepository stockCategoryJpaRepository;
	private final StockCategoryRepository stockCategoryRepository;
	private final StockCategorySaveController stockCategorySaveController;
	private final StockCateogoryToDtoStockCategoryMapper stockCateogoryToDtoStockCategoryMapper;
	
	@Autowired
	public StockCategoryService(
			StockCategoryJpaRepository stockCategoryJpaRepository,
			StockCategoryRepository stockCategoryRepository,
			StockCategorySaveController stockCategorySaveController,
			StockCateogoryToDtoStockCategoryMapper stockCateogoryToDtoStockCategoryMapper) {
		
		this.stockCategoryJpaRepository = stockCategoryJpaRepository;
		this.stockCategoryRepository = stockCategoryRepository;
		this.stockCategorySaveController = stockCategorySaveController;
		this.stockCateogoryToDtoStockCategoryMapper = stockCateogoryToDtoStockCategoryMapper;
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

        return stockCategoryRepository.loadOverview(pagingParams);
    }
	
	@Override
    public void markAsDeleted(IAsyncMonitor monitor, Long categoryId) {
        LOGGER.info("Marking stock category '{}' as deleted.", categoryId);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_DELETE_CATEGORY));

        StockCategory category = stockCategoryJpaRepository.getOne(categoryId);
        category.setDeletionDate(LocalDateTime.now());
	    stockCategoryJpaRepository.save(category);
    }
	
	@Override
	public List<DtoStockCategory> loadAllStockCategories(IAsyncMonitor monitor) {
		LOGGER.info("Loading all stock categories.");
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_ALL_STOCK_CATEGORIES));
		
		List<StockCategory> stockCategories = stockCategoryJpaRepository.findByDeletionDateIsNull();
		return stockCateogoryToDtoStockCategoryMapper.mapAll(stockCategories);
	}
	
	@Override
	public List<String> loadAllStockCategoryNames() {
		return stockCategoryJpaRepository.findByDeletionDateIsNull().stream()
				.map(StockCategory::getName)
				.collect(Collectors.toList());
	}
}