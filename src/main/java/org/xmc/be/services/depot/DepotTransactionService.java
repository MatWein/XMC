package org.xmc.be.services.depot;

import com.querydsl.core.QueryResults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransaction;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransactionOverview;
import org.xmc.fe.async.AsyncMonitor;

import java.util.Set;

@Service
@Transactional
public class DepotTransactionService {
	public QueryResults<DtoDepotTransactionOverview> loadOverview(
			AsyncMonitor monitor,
			long depotId,
			PagingParams<DepotTransactionOverviewFields> pagingParams) {
		
		return null;
	}
	
	public void saveOrUpdate(AsyncMonitor monitor, long depotId, DtoDepotTransaction dtoDepotTransaction) {
	
	}
	
	public void markAsDeleted(AsyncMonitor monitor, Set<Long> selectedDepotTransactionIds) {
	
	}
}
