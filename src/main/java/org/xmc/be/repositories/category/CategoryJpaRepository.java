package org.xmc.be.repositories.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.Category;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
}
