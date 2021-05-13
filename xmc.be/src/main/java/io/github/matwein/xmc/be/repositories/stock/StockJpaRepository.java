package io.github.matwein.xmc.be.repositories.stock;

import io.github.matwein.xmc.be.entities.depot.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockJpaRepository extends JpaRepository<Stock, Long> {
}
