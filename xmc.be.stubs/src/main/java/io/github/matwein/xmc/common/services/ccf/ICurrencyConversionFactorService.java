package io.github.matwein.xmc.common.services.ccf;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.ccf.CurrencyConversionFactorOverviewFields;
import io.github.matwein.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;

public interface ICurrencyConversionFactorService {
	QueryResults<DtoCurrencyConversionFactor> loadOverview(IAsyncMonitor monitor, PagingParams<CurrencyConversionFactorOverviewFields> pagingParams);
	
	void saveOrUpdate(IAsyncMonitor monitor, DtoCurrencyConversionFactor dtoCurrencyConversionFactor);
	
	void delete(IAsyncMonitor monitor, long currencyConversionFactorId);
}
