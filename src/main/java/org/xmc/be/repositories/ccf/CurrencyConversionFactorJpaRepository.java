package org.xmc.be.repositories.ccf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.depot.CurrencyConversionFactor;

import java.util.Collection;
import java.util.List;

public interface CurrencyConversionFactorJpaRepository extends JpaRepository<CurrencyConversionFactor, Long> {
	List<CurrencyConversionFactor> findByCurrencyIn(Collection<String> currencies);
}
