package org.xmc.be.services.category;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.depot.StockCategory;
import org.xmc.be.repositories.category.StockCategoryJpaRepository;
import org.xmc.be.repositories.category.StockCategoryRepository;
import org.xmc.be.services.category.controller.StockCategorySaveController;
import org.xmc.be.services.category.mapper.StockCateogoryToDtoStockCategoryMapper;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.category.DtoStockCategory;
import org.xmc.common.stubs.category.DtoStockCategoryOverview;
import org.xmc.common.stubs.category.StockCategoryOverviewFields;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class StockCategoryService {
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
	
	public void saveOrUpdate(AsyncMonitor monitor, DtoStockCategory dtoCategory) {
        LOGGER.info("Saving stock category: {}", dtoCategory);
        monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_CATEGORY);

        stockCategorySaveController.saveOrUpdate(dtoCategory);
    }

    public QueryResults<DtoStockCategoryOverview> loadOverview(AsyncMonitor monitor, PagingParams<StockCategoryOverviewFields> pagingParams) {
        LOGGER.info("Loading stock category overview: {}", pagingParams);
        monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_CATEGORY_OVERVIEW);

        return stockCategoryRepository.loadOverview(pagingParams);
    }

    public void markAsDeleted(AsyncMonitor monitor, Long categoryId) {
        LOGGER.info("Marking stock category '{}' as deleted.", categoryId);
        monitor.setStatusText(MessageKey.ASYNC_TASK_DELETE_CATEGORY);

        StockCategory category = stockCategoryJpaRepository.getOne(categoryId);
        category.setDeletionDate(LocalDateTime.now());
	    stockCategoryJpaRepository.save(category);
    }
	
	public List<DtoStockCategory> loadAllStockCategories(AsyncMonitor monitor) {
		LOGGER.info("Loading all stock categories.");
		monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_ALL_STOCK_CATEGORIES);
		
		List<StockCategory> stockCategories = stockCategoryJpaRepository.findByDeletionDateIsNull();
		return stockCateogoryToDtoStockCategoryMapper.mapAll(stockCategories);
	}
}
