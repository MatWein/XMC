package org.xmc.be.repositories.depot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.depot.DepotTransaction;

public interface DepotTransactionJpaRepository extends JpaRepository<DepotTransaction, Long> {
}
