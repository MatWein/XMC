package org.xmc.be.repositories.depot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.depot.DepotItem;

public interface DepotItemJpaRepository extends JpaRepository<DepotItem, Long> {
}
