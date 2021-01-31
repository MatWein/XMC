package org.xmc.be.repositories.depot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.depot.DepotDelivery;

public interface DepotDeliveryJpaRepository extends JpaRepository<DepotDelivery, Long> {
}
