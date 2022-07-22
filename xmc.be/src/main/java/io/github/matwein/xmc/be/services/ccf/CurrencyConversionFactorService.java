package io.github.matwein.xmc.be.services.ccf;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.common.mapper.QueryResultsMapper;
import io.github.matwein.xmc.be.entities.depot.CurrencyConversionFactor;
import io.github.matwein.xmc.be.repositories.ccf.CurrencyConversionFactorJpaRepository;
import io.github.matwein.xmc.be.repositories.ccf.CurrencyConversionFactorRepository;
import io.github.matwein.xmc.be.services.ccf.controller.CurrencyConversionFactorSaveController;
import io.github.matwein.xmc.be.services.depot.controller.DeliverySaldoUpdatingController;
import io.github.matwein.xmc.common.services.ccf.ICurrencyConversionFactorService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.ccf.CurrencyConversionFactorOverviewFields;
import io.github.matwein.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CurrencyConversionFactorService implements ICurrencyConversionFactorService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyConversionFactorService.class);
	
	private final CurrencyConversionFactorJpaRepository currencyConversionFactorJpaRepository;
	private final CurrencyConversionFactorSaveController currencyConversionFactorSaveController;
	private final CurrencyConversionFactorRepository currencyConversionFactorRepository;
	private final DeliverySaldoUpdatingController deliverySaldoUpdatingController;
	private final QueryResultsMapper queryResultsMapper;
	
	@Autowired
	public CurrencyConversionFactorService(
			CurrencyConversionFactorJpaRepository currencyConversionFactorJpaRepository,
			CurrencyConversionFactorSaveController currencyConversionFactorSaveController,
			CurrencyConversionFactorRepository currencyConversionFactorRepository,
			DeliverySaldoUpdatingController deliverySaldoUpdatingController,
			QueryResultsMapper queryResultsMapper) {
		
		this.currencyConversionFactorJpaRepository = currencyConversionFactorJpaRepository;
		this.currencyConversionFactorSaveController = currencyConversionFactorSaveController;
		this.currencyConversionFactorRepository = currencyConversionFactorRepository;
		this.deliverySaldoUpdatingController = deliverySaldoUpdatingController;
		this.queryResultsMapper = queryResultsMapper;
	}
	
	@Override
	public QueryResults<DtoCurrencyConversionFactor> loadOverview(IAsyncMonitor monitor, PagingParams<CurrencyConversionFactorOverviewFields> pagingParams) {
		LOGGER.info("Loading currency conversion factor overview: {}", pagingParams);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_CURRENCY_CONVERSION_FACTOR_OVERVIEW));
		
		var results = currencyConversionFactorRepository.loadOverview(pagingParams);
		return queryResultsMapper.map(results);
	}
	
	@Override
	public void saveOrUpdate(IAsyncMonitor monitor, DtoCurrencyConversionFactor dtoCurrencyConversionFactor) {
		LOGGER.info("Saving currency conversion factor: {}", dtoCurrencyConversionFactor);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_CURRENCY_CONVERSION_FACTOR));
		
		currencyConversionFactorSaveController.saveOrUpdate(dtoCurrencyConversionFactor);
		deliverySaldoUpdatingController.updateDeliverySaldoForAllDeliveries(dtoCurrencyConversionFactor.getInputDate());
	}
	
	@Override
	public void delete(IAsyncMonitor monitor, long currencyConversionFactorId) {
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_DELETE_CURRENCY_CONVERSION_FACTOR));
		
		CurrencyConversionFactor existingConversionFactor = currencyConversionFactorJpaRepository.getReferenceById(currencyConversionFactorId);
		LOGGER.info("Deleting currency conversion factor '{}'.", existingConversionFactor);
	
		currencyConversionFactorJpaRepository.delete(existingConversionFactor);
		deliverySaldoUpdatingController.updateDeliverySaldoForAllDeliveries(existingConversionFactor.getInputDate());
	}
}
