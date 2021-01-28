package org.xmc.be.services.ccf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.CurrencyConversionFactor;
import org.xmc.be.repositories.ccf.CurrencyConversionFactorJpaRepository;
import org.xmc.be.services.ccf.mapper.DtoCurrencyConversionFactorToCurrencyConversionFactorMapper;
import org.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;

@Component
public class CurrencyConversionFactorSaveController {
	private final CurrencyConversionFactorJpaRepository currencyConversionFactorJpaRepository;
	private final DtoCurrencyConversionFactorToCurrencyConversionFactorMapper dtoCurrencyConversionFactorToCurrencyConversionFactorMapper;
	
	@Autowired
	public CurrencyConversionFactorSaveController(
			CurrencyConversionFactorJpaRepository currencyConversionFactorJpaRepository,
			DtoCurrencyConversionFactorToCurrencyConversionFactorMapper dtoCurrencyConversionFactorToCurrencyConversionFactorMapper) {
		
		this.currencyConversionFactorJpaRepository = currencyConversionFactorJpaRepository;
		this.dtoCurrencyConversionFactorToCurrencyConversionFactorMapper = dtoCurrencyConversionFactorToCurrencyConversionFactorMapper;
	}
	
	public void saveOrUpdate(DtoCurrencyConversionFactor dtoCurrencyConversionFactor) {
		CurrencyConversionFactor conversionFactor = createOrUpdateConversionFactor(dtoCurrencyConversionFactor);
		currencyConversionFactorJpaRepository.save(conversionFactor);
	}
	
	private CurrencyConversionFactor createOrUpdateConversionFactor(DtoCurrencyConversionFactor dtoCurrencyConversionFactor) {
		if (dtoCurrencyConversionFactor.getId() == null) {
			return dtoCurrencyConversionFactorToCurrencyConversionFactorMapper.map(dtoCurrencyConversionFactor);
		} else {
			CurrencyConversionFactor conversionFactor = currencyConversionFactorJpaRepository.getOne(dtoCurrencyConversionFactor.getId());
			dtoCurrencyConversionFactorToCurrencyConversionFactorMapper.update(conversionFactor, dtoCurrencyConversionFactor);
			return conversionFactor;
		}
	}
}
