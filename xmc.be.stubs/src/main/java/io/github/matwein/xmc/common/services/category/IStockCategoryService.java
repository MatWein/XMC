package io.github.matwein.xmc.common.services.category;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.category.DtoStockCategory;
import io.github.matwein.xmc.common.stubs.category.DtoStockCategoryOverview;
import io.github.matwein.xmc.common.stubs.category.StockCategoryOverviewFields;

import java.util.List;

public interface IStockCategoryService {
	void saveOrUpdate(IAsyncMonitor monitor, DtoStockCategory dtoCategory);
	
	QueryResults<DtoStockCategoryOverview> loadOverview(IAsyncMonitor monitor, PagingParams<StockCategoryOverviewFields> pagingParams);
	
	void markAsDeleted(IAsyncMonitor monitor, Long categoryId);
	
	List<DtoStockCategoryOverview> loadAllStockCategories(IAsyncMonitor monitor);
	
	List<String> loadAllStockCategoryNames();
}
