package org.xmc.be.repositories.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.depot.Stock;

public interface StockJpaRepository extends JpaRepository<Stock, Long> {
}
