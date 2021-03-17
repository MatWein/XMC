package org.xmc.be.repositories.depot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotTransaction;

import java.util.List;

public interface DepotTransactionJpaRepository extends JpaRepository<DepotTransaction, Long> {
	List<DepotTransaction> findByDepotAndDeletionDateIsNull(Depot depot);
}
