package io.github.matwein.xmc.be.repositories.depot;

import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DepotDeliveryJpaRepository extends JpaRepository<DepotDelivery, Long> {
	List<DepotDelivery> findByDeliveryDateGreaterThanEqualAndDeletionDateIsNull(LocalDateTime deliveryDate);
	
	List<DepotDelivery> findByDepotAndDeletionDateIsNull(Depot depot);
	
	@Query(
		"""
		SELECT del FROM DepotDelivery del
		WHERE del.deletionDate IS NULL AND del.depot.id in (:depotIds)
		ORDER BY del.deliveryDate ASC, del.creationDate ASC, del.id ASC
		"""
	)
	List<DepotDelivery> findAllDeliveries(Collection<Long> depotIds, Pageable pageable);
	
	default Optional<DepotDelivery> findFirstDelivery(Collection<Long> depotIds) {
		return findAllDeliveries(depotIds, PageRequest.of(0, 1)).stream().findFirst();
	}
}
