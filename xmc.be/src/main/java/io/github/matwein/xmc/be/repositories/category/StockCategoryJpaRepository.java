package io.github.matwein.xmc.be.repositories.category;

import io.github.matwein.xmc.be.entities.depot.StockCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockCategoryJpaRepository extends JpaRepository<StockCategory, Long> {
}
