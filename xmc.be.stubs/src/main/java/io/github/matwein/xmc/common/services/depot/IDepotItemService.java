package io.github.matwein.xmc.common.services.depot;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItem;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemOverview;

import java.util.Set;

public interface IDepotItemService {
	QueryResults<DtoDepotItemOverview> loadOverview(
			IAsyncMonitor monitor,
			long depotDeliveryId,
			PagingParams<DepotItemOverviewFields> pagingParams);
	
	void saveOrUpdate(IAsyncMonitor monitor, long depotDeliveryId, DtoDepotItem dtoDepotItem);
	
	void markAsDeleted(IAsyncMonitor monitor, long depotDeliveryId, Set<Long> depotItemIds);
}
