package io.github.matwein.xmc.be.services.ccf.controller;

import io.github.matwein.xmc.be.entities.depot.CurrencyConversionFactor;
import io.github.matwein.xmc.be.repositories.ccf.CurrencyConversionFactorJpaRepository;
import io.github.matwein.xmc.be.services.ccf.mapper.DtoCurrencyConversionFactorToCurrencyConversionFactorMapper;
import io.github.matwein.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
