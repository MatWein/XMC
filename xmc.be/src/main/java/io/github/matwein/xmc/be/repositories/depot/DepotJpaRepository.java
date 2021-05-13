package io.github.matwein.xmc.be.repositories.depot;

import io.github.matwein.xmc.be.entities.depot.Depot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepotJpaRepository extends JpaRepository<Depot, Long> {
	List<Depot> findByDeletionDateIsNull();
}
