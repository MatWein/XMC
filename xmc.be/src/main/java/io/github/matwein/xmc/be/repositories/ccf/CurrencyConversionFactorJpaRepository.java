package io.github.matwein.xmc.be.repositories.ccf;

import io.github.matwein.xmc.be.entities.depot.CurrencyConversionFactor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CurrencyConversionFactorJpaRepository extends JpaRepository<CurrencyConversionFactor, Long> {
	List<CurrencyConversionFactor> findByCurrencyIn(Collection<String> currencies);
}
