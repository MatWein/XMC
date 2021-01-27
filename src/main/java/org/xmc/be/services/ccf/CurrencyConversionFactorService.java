package org.xmc.be.services.ccf;

import com.querydsl.core.QueryResults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.ccf.CurrencyConversionFactorOverviewFields;
import org.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;
import org.xmc.fe.async.AsyncMonitor;

@Service
@Transactional
public class CurrencyConversionFactorService {
	public QueryResults<DtoCurrencyConversionFactor> loadOverview(AsyncMonitor monitor, PagingParams<CurrencyConversionFactorOverviewFields> pagingParams) {
		return null;
	}
	
	public void saveOrUpdate(AsyncMonitor monitor, DtoCurrencyConversionFactor dtoCurrencyConversionFactor) {
	
	}
	
	public void delete(AsyncMonitor monitor, long currencyConversionFactorId) {
	
	}
}
