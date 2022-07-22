package io.github.matwein.xmc.be.repositories.depot;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class DepotTransactionJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private DepotTransactionJpaRepository repository;
	
	@Test
	void testFindByDepotAndDeletionDateIsNull() {
		Depot depot = graphGenerator.createDepot();
		
		graphGenerator.createDepotTransaction();
		
		DepotTransaction depotTransaction1 = graphGenerator.createDepotTransaction(depot);
		
		DepotTransaction depotTransaction2 = graphGenerator.createDepotTransaction(depot);
		depotTransaction2.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depotTransaction2);
		
		DepotTransaction depotTransaction3 = graphGenerator.createDepotTransaction(depot);
		
		graphGenerator.createDepotTransaction();
		
		flushAndClear();
		
		List<DepotTransaction> result = repository.findByDepotAndDeletionDateIsNull(depot);
		
		Assertions.assertEquals(Set.of(depotTransaction1, depotTransaction3), new HashSet<>(result));
	}
	
	@Test
	void testFindMostRecentTransactions() {
		Depot depot = graphGenerator.createDepot();
		
		graphGenerator.createDepotTransaction();
		
		DepotTransaction depotTransaction1 = graphGenerator.createDepotTransaction(depot);
		depotTransaction1.setValutaDate(LocalDate.of(2021, Month.NOVEMBER, 15));
		session().saveOrUpdate(depotTransaction1);
		
		DepotTransaction depotTransaction2 = graphGenerator.createDepotTransaction(depot);
		depotTransaction2.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depotTransaction2);
		
		DepotTransaction depotTransaction3 = graphGenerator.createDepotTransaction(depot);
		depotTransaction3.setValutaDate(LocalDate.of(2021, Month.NOVEMBER, 16));
		session().saveOrUpdate(depotTransaction3);
		
		graphGenerator.createDepotTransaction();
		
		flushAndClear();
		
		List<DepotTransaction> result = repository.findMostRecentTransactions(Set.of(depot.getId()), PageRequest.of(0, 1));
		
		Assertions.assertEquals(List.of(depotTransaction3), result);
	}
}