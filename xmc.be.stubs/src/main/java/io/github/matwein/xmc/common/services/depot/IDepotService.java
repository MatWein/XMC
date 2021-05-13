package io.github.matwein.xmc.common.services.depot;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.depot.DepotOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.DtoDepot;
import io.github.matwein.xmc.common.stubs.depot.DtoDepotOverview;

public interface IDepotService {
	QueryResults<DtoDepotOverview> loadOverview(IAsyncMonitor monitor, PagingParams<DepotOverviewFields> pagingParams);
	
	void saveOrUpdate(IAsyncMonitor monitor, DtoDepot dtoDepot);
	
	void markAsDeleted(IAsyncMonitor monitor, long depotId);
}
