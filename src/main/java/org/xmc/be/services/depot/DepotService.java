package org.xmc.be.services.depot;

import com.querydsl.core.QueryResults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.depot.DepotOverviewFields;
import org.xmc.common.stubs.depot.DtoDepot;
import org.xmc.common.stubs.depot.DtoDepotOverview;
import org.xmc.fe.async.AsyncMonitor;

@Service
@Transactional
public class DepotService {
	public QueryResults<DtoDepotOverview> loadOverview(AsyncMonitor monitor, PagingParams<DepotOverviewFields> pagingParams) {
		return null;
	}
	
	public void saveOrUpdate(AsyncMonitor monitor, DtoDepot dtoDepot) {
	
	}
	
	public void markAsDeleted(AsyncMonitor monitor, long depotId) {
	
	}
}
