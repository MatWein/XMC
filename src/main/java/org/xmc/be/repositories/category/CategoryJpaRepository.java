package org.xmc.be.repositories.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.Category;

import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    List<Category> findByDeletionDateIsNull();
}
