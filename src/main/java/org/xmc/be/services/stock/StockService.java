package org.xmc.be.services.stock;

import com.querydsl.core.QueryResults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.stocks.DtoStock;
import org.xmc.common.stubs.stocks.DtoStockOverview;
import org.xmc.common.stubs.stocks.StockOverviewFields;
import org.xmc.fe.async.AsyncMonitor;

@Service
@Transactional
public class StockService {
	public void saveOrUpdate(AsyncMonitor monitor, DtoStock dtoStock) {
	
	}
	
	public QueryResults<DtoStockOverview> loadOverview(AsyncMonitor monitor, PagingParams<StockOverviewFields> pagingParams) {
		return null;
	}
}
