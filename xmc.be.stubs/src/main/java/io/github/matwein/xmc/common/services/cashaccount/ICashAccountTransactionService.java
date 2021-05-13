package io.github.matwein.xmc.common.services.cashaccount;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface ICashAccountTransactionService {
	QueryResults<DtoCashAccountTransactionOverview> loadOverview(
			IAsyncMonitor monitor,
			long cashAccountId,
			PagingParams<CashAccountTransactionOverviewFields> pagingParams);
	
	void markAsDeleted(IAsyncMonitor monitor, Collection<Long> transactionIds);
	
	void saveOrUpdate(IAsyncMonitor monitor, long cashAccountId, DtoCashAccountTransaction dtoCashAccountTransaction);
	
	Optional<Long> autoDetectCategory(
			IAsyncMonitor monitor,
			long cashAccountId,
			String usage);
	
	Pair<BigDecimal, BigDecimal> calculateSaldoPreview(long cashAccountId, Long transactionId, LocalDate valutaDate, BigDecimal value);
}
