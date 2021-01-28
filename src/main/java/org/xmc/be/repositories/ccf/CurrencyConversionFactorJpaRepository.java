package org.xmc.be.repositories.ccf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.depot.CurrencyConversionFactor;

public interface CurrencyConversionFactorJpaRepository extends JpaRepository<CurrencyConversionFactor, Long> {
}
