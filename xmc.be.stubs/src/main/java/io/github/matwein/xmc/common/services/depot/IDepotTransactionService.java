package io.github.matwein.xmc.common.services.depot;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransaction;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransactionOverview;

import java.util.Set;

public interface IDepotTransactionService {
	QueryResults<DtoDepotTransactionOverview> loadOverview(
			IAsyncMonitor monitor,
			long depotId,
			PagingParams<DepotTransactionOverviewFields> pagingParams);
	
	void saveOrUpdate(IAsyncMonitor monitor, long depotId, DtoDepotTransaction dtoDepotTransaction);
	
	void markAsDeleted(IAsyncMonitor monitor, Set<Long> transactionIds);
}
