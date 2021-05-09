package io.github.matwein.xmc.be.repositories.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.matwein.xmc.be.entities.depot.Stock;

public interface StockJpaRepository extends JpaRepository<Stock, Long> {
}
