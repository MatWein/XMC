package io.github.matwein.xmc.common.services.cashaccount;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.DtoCashAccount;
import io.github.matwein.xmc.common.stubs.cashaccount.DtoCashAccountOverview;

public interface ICashAccountService {
	QueryResults<DtoCashAccountOverview> loadOverview(IAsyncMonitor monitor, PagingParams<CashAccountOverviewFields> pagingParams);
	
	void saveOrUpdate(IAsyncMonitor monitor, DtoCashAccount dtoCashAccount);
	
	void markAsDeleted(IAsyncMonitor monitor, Long cashAccountId);
}
