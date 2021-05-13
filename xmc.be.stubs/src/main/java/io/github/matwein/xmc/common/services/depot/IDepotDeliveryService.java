package io.github.matwein.xmc.common.services.depot;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDelivery;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryOverview;

public interface IDepotDeliveryService {
	QueryResults<DtoDepotDeliveryOverview> loadOverview(
			IAsyncMonitor monitor,
			long depotId,
			PagingParams<DepotDeliveryOverviewFields> pagingParams);
	
	void saveOrUpdate(IAsyncMonitor monitor, long depotId, DtoDepotDelivery dtoDepotDelivery);
	
	void markAsDeleted(IAsyncMonitor monitor, long deliveryId);
}
