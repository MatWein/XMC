package io.github.matwein.xmc.common.services.stock;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.stocks.DtoMinimalStock;
import io.github.matwein.xmc.common.stubs.stocks.DtoStock;
import io.github.matwein.xmc.common.stubs.stocks.DtoStockOverview;
import io.github.matwein.xmc.common.stubs.stocks.StockOverviewFields;

import java.util.List;

public interface IStockService {
	QueryResults<DtoStockOverview> loadOverview(IAsyncMonitor monitor, PagingParams<StockOverviewFields> pagingParams);
	
	void saveOrUpdate(IAsyncMonitor monitor, DtoStock dtoStock);
	
	void deleteStock(IAsyncMonitor monitor, long stockId);
	
	List<String> loadAllStockIsins();
	
	List<DtoMinimalStock> loadAllStocks(String searchValue, int limit);
}
