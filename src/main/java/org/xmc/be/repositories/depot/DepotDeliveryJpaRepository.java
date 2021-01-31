package org.xmc.be.repositories.depot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.depot.DepotDelivery;

import java.time.LocalDateTime;
import java.util.List;

public interface DepotDeliveryJpaRepository extends JpaRepository<DepotDelivery, Long> {
	List<DepotDelivery> findByDeliveryDateGreaterThanEqualAndDeletionDateIsNull(LocalDateTime deliveryDate);
}
