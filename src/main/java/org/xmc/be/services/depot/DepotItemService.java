package org.xmc.be.services.depot;

import com.querydsl.core.QueryResults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.depot.deliveries.DepotItemOverviewFields;
import org.xmc.common.stubs.depot.deliveries.DtoDepotItemOverview;
import org.xmc.fe.async.AsyncMonitor;

@Service
@Transactional
public class DepotItemService {
	public QueryResults<DtoDepotItemOverview> loadOverview(
			AsyncMonitor monitor,
			long depotDeliveryId,
			PagingParams<DepotItemOverviewFields> pagingParams) {
		
		return null;
	}
}
