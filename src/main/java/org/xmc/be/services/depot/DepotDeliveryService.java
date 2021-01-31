package org.xmc.be.services.depot;

import com.querydsl.core.QueryResults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryOverview;
import org.xmc.fe.async.AsyncMonitor;

@Service
@Transactional
public class DepotDeliveryService {
	public QueryResults<DtoDepotDeliveryOverview> loadOverview(AsyncMonitor monitor, long depotId, PagingParams<DepotDeliveryOverviewFields> pagingParams) {
		return null;
	}
}
