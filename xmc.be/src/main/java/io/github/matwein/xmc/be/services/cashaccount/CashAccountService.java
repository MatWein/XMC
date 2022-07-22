package io.github.matwein.xmc.be.services.cashaccount;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.common.mapper.QueryResultsMapper;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountRepository;
import io.github.matwein.xmc.be.services.cashaccount.controller.CashAccountSaveController;
import io.github.matwein.xmc.common.services.cashaccount.ICashAccountService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.cashaccount.CashAccountOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.DtoCashAccount;
import io.github.matwein.xmc.common.stubs.cashaccount.DtoCashAccountOverview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class CashAccountService implements ICashAccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CashAccountService.class);

    private final CashAccountRepository cashAccountRepository;
    private final CashAccountSaveController cashAccountSaveController;
    private final CashAccountJpaRepository cashAccountJpaRepository;
	private final QueryResultsMapper queryResultsMapper;
	
	@Autowired
    public CashAccountService(
		    CashAccountRepository cashAccountRepository,
		    CashAccountSaveController cashAccountSaveController,
		    CashAccountJpaRepository cashAccountJpaRepository,
		    QueryResultsMapper queryResultsMapper) {

        this.cashAccountRepository = cashAccountRepository;
        this.cashAccountSaveController = cashAccountSaveController;
        this.cashAccountJpaRepository = cashAccountJpaRepository;
		this.queryResultsMapper = queryResultsMapper;
	}
	
	@Override
	public QueryResults<DtoCashAccountOverview> loadOverview(IAsyncMonitor monitor, PagingParams<CashAccountOverviewFields> pagingParams) {
		LOGGER.info("Loading cash account overview: {}", pagingParams);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_CASHACCOUNT_OVERVIEW));
		
		var results = cashAccountRepository.loadOverview(pagingParams);
		return queryResultsMapper.map(results);
	}
	
	@Override
    public void saveOrUpdate(IAsyncMonitor monitor, DtoCashAccount dtoCashAccount) {
        LOGGER.info("Saving cash account: {}", dtoCashAccount);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_CASHACCOUNT));

        cashAccountSaveController.saveOrUpdate(dtoCashAccount);
    }

    @Override
    public void markAsDeleted(IAsyncMonitor monitor, Long cashAccountId) {
        LOGGER.info("Marking cash account '{}' as deleted.", cashAccountId);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_DELETE_CASHACCOUNT));

        CashAccount cashAccount = cashAccountJpaRepository.getReferenceById(cashAccountId);
        cashAccount.setDeletionDate(LocalDateTime.now());
        cashAccountJpaRepository.save(cashAccount);
    }
}
