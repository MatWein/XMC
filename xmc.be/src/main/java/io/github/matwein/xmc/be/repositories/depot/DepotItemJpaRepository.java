package io.github.matwein.xmc.be.repositories.depot;

import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.be.entities.depot.DepotItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepotItemJpaRepository extends JpaRepository<DepotItem, Long> {
	List<DepotItem> findByDeliveryAndDeletionDateIsNull(DepotDelivery delivery);
}
