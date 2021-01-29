package org.xmc.be.repositories.depot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.depot.Depot;

public interface DepotJpaRepository extends JpaRepository<Depot, Long> {
}
