package org.xmc.be.repositories.depot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.depot.Depot;

import java.util.List;

public interface DepotJpaRepository extends JpaRepository<Depot, Long> {
	List<Depot> findByDeletionDateIsNull();
}
