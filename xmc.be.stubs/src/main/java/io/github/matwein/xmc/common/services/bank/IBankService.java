package io.github.matwein.xmc.common.services.bank;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.bank.BankOverviewFields;
import io.github.matwein.xmc.common.stubs.bank.DtoBank;
import io.github.matwein.xmc.common.stubs.bank.DtoBankOverview;

import java.util.List;

public interface IBankService {
	List<DtoBank> loadAllBanks(IAsyncMonitor monitor);
	
	void saveOrUpdate(IAsyncMonitor monitor, DtoBank dtoBank);
	
	QueryResults<DtoBankOverview> loadOverview(IAsyncMonitor monitor, PagingParams<BankOverviewFields> pagingParams);
	
	void markAsDeleted(IAsyncMonitor monitor, long bankId);
}
