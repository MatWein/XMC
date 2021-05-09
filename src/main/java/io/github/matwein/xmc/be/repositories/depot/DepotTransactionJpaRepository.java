package io.github.matwein.xmc.be.repositories.depot;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotTransaction;

import java.util.Collection;
import java.util.List;

public interface DepotTransactionJpaRepository extends JpaRepository<DepotTransaction, Long> {
	List<DepotTransaction> findByDepotAndDeletionDateIsNull(Depot depot);
	
	@Query("SELECT dt FROM DepotTransaction dt " +
			"WHERE dt.deletionDate IS NULL AND dt.depot.id in (:depotIds) " +
			"ORDER BY dt.valutaDate DESC, dt.creationDate DESC, dt.id DESC"
	)
	List<DepotTransaction> findMostRecentTransactions(Collection<Long> depotIds, Pageable pageable);
}
