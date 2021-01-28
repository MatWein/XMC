package org.xmc.be.services.ccf;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.depot.CurrencyConversionFactor;
import org.xmc.be.repositories.ccf.CurrencyConversionFactorJpaRepository;
import org.xmc.be.repositories.ccf.CurrencyConversionFactorRepository;
import org.xmc.be.services.ccf.controller.CurrencyConversionFactorSaveController;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.ccf.CurrencyConversionFactorOverviewFields;
import org.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.Optional;

@Service
@Transactional
public class CurrencyConversionFactorService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyConversionFactorService.class);
	
	private final CurrencyConversionFactorJpaRepository currencyConversionFactorJpaRepository;
	private final CurrencyConversionFactorSaveController currencyConversionFactorSaveController;
	private final CurrencyConversionFactorRepository currencyConversionFactorRepository;
	
	@Autowired
	public CurrencyConversionFactorService(
			CurrencyConversionFactorJpaRepository currencyConversionFactorJpaRepository,
			CurrencyConversionFactorSaveController currencyConversionFactorSaveController,
			CurrencyConversionFactorRepository currencyConversionFactorRepository) {
		
		this.currencyConversionFactorJpaRepository = currencyConversionFactorJpaRepository;
		this.currencyConversionFactorSaveController = currencyConversionFactorSaveController;
		this.currencyConversionFactorRepository = currencyConversionFactorRepository;
	}
	
	public QueryResults<DtoCurrencyConversionFactor> loadOverview(AsyncMonitor monitor, PagingParams<CurrencyConversionFactorOverviewFields> pagingParams) {
		LOGGER.info("Loading currency conversion factor overview: {}", pagingParams);
		monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_CURRENCY_CONVERSION_FACTOR_OVERVIEW);
		
		return currencyConversionFactorRepository.loadOverview(pagingParams);
	}
	
	public void saveOrUpdate(AsyncMonitor monitor, DtoCurrencyConversionFactor dtoCurrencyConversionFactor) {
		LOGGER.info("Saving currency conversion factor: {}", dtoCurrencyConversionFactor);
		monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_CURRENCY_CONVERSION_FACTOR);
		
		currencyConversionFactorSaveController.saveOrUpdate(dtoCurrencyConversionFactor);
	}
	
	public void delete(AsyncMonitor monitor, long currencyConversionFactorId) {
		monitor.setStatusText(MessageKey.ASYNC_TASK_DELETE_CURRENCY_CONVERSION_FACTOR);
		
		Optional<CurrencyConversionFactor> existingConversionFactor = currencyConversionFactorJpaRepository.findById(currencyConversionFactorId);
		if (existingConversionFactor.isPresent()) {
			LOGGER.info("Deleting currency conversion factor '{}'.", existingConversionFactor.get());
			currencyConversionFactorJpaRepository.delete(existingConversionFactor.get());
		}
	}
}
