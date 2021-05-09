package io.github.matwein.xmc.be.repositories.category;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.matwein.xmc.be.entities.cashaccount.Category;

import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    List<Category> findByDeletionDateIsNull();
}
