package io.github.matwein.xmc.common.services.category;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.category.CategoryOverviewFields;
import io.github.matwein.xmc.common.stubs.category.DtoCategory;
import io.github.matwein.xmc.common.stubs.category.DtoCategoryOverview;

import java.util.List;

public interface ICategoryService {
	List<DtoCategory> loadAllCategories(IAsyncMonitor monitor);
	
	void saveOrUpdate(IAsyncMonitor monitor, DtoCategory dtoCategory);
	
	QueryResults<DtoCategoryOverview> loadOverview(IAsyncMonitor monitor, PagingParams<CategoryOverviewFields> pagingParams);
	
	void markAsDeleted(IAsyncMonitor monitor, Long categoryId);
}
