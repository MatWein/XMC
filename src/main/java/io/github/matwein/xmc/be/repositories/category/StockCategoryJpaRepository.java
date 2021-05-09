package io.github.matwein.xmc.be.repositories.category;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.matwein.xmc.be.entities.depot.StockCategory;

import java.util.List;

public interface StockCategoryJpaRepository extends JpaRepository<StockCategory, Long> {
    List<StockCategory> findByDeletionDateIsNull();
}
