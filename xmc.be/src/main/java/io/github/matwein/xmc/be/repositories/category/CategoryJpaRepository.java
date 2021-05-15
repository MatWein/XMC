package io.github.matwein.xmc.be.repositories.category;

import io.github.matwein.xmc.be.entities.cashaccount.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
}
