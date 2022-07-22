package io.github.matwein.xmc.be.repositories.depot;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

class DepotDeliveryJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private DepotDeliveryJpaRepository repository;
	
	@Test
	void testFindByDeliveryDateGreaterThanEqualAndDeletionDateIsNull() {
		Depot depot = graphGenerator.createDepot();
		
		graphGenerator.createDepotDelivery(
				depot,
				LocalDateTime.of(2020, Month.NOVEMBER, 15, 0, 0, 0),
				BigDecimal.valueOf(1000.0));
		
		DepotDelivery depotDelivery2 = graphGenerator.createDepotDelivery(
				depot,
				LocalDateTime.of(2021, Month.NOVEMBER, 16, 0, 0, 0),
				BigDecimal.valueOf(1000.0));
		
		DepotDelivery depotDelivery3 = graphGenerator.createDepotDelivery(
				depot,
				LocalDateTime.of(2021, Month.NOVEMBER, 15, 0, 0, 0),
				BigDecimal.valueOf(1000.0));
		
		DepotDelivery depotDelivery4 = graphGenerator.createDepotDelivery(
				depot,
				LocalDateTime.of(2021, Month.NOVEMBER, 17, 0, 0, 0),
				BigDecimal.valueOf(1000.0));
		depotDelivery4.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depotDelivery4);
		
		flushAndClear();
		
		List<DepotDelivery> result = repository.findByDeliveryDateGreaterThanEqualAndDeletionDateIsNull(
				LocalDateTime.of(2021, Month.NOVEMBER, 15, 0, 0, 0));
		
		Assertions.assertEquals(Set.of(depotDelivery2, depotDelivery3), new HashSet<>(result));
	}
	
	@Test
	void testFindByDepotAndDeletionDateIsNull() {
		Depot depot = graphGenerator.createDepot();
		
		graphGenerator.createDepotDelivery();
		
		DepotDelivery depotDelivery1 = graphGenerator.createDepotDelivery(
				depot,
				LocalDateTime.of(2020, Month.NOVEMBER, 15, 0, 0, 0),
				BigDecimal.valueOf(1000.0));
		
		DepotDelivery depotDelivery2 = graphGenerator.createDepotDelivery(
				depot,
				LocalDateTime.of(2021, Month.NOVEMBER, 16, 0, 0, 0),
				BigDecimal.valueOf(1000.0));
		
		DepotDelivery depotDelivery3 = graphGenerator.createDepotDelivery(
				depot,
				LocalDateTime.of(2021, Month.NOVEMBER, 15, 0, 0, 0),
				BigDecimal.valueOf(1000.0));
		
		DepotDelivery depotDelivery4 = graphGenerator.createDepotDelivery(
				depot,
				LocalDateTime.of(2021, Month.NOVEMBER, 17, 0, 0, 0),
				BigDecimal.valueOf(1000.0));
		depotDelivery4.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depotDelivery4);
		
		graphGenerator.createDepotDelivery();
		
		flushAndClear();
		
		List<DepotDelivery> result = repository.findByDepotAndDeletionDateIsNull(depot);
		
		Assertions.assertEquals(Set.of(depotDelivery1, depotDelivery2, depotDelivery3), new HashSet<>(result));
	}
	
	@Test
	void testFindAllDeliveries() {
		Depot depot1 = graphGenerator.createDepot();
		Depot depot2 = graphGenerator.createDepot();
		
		DepotDelivery depotDelivery0 = graphGenerator.createDepotDelivery(
				depot1,
				LocalDateTime.of(2021, Month.NOVEMBER, 14, 0, 0, 0),
				BigDecimal.valueOf(500.0));
		depotDelivery0.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depotDelivery0);
		
		DepotDelivery depotDelivery1 = graphGenerator.createDepotDelivery(
				depot1,
				LocalDateTime.of(2021, Month.NOVEMBER, 15, 0, 0, 0),
				BigDecimal.valueOf(1000.0));
		
		graphGenerator.createDepotDelivery(
				depot1,
				LocalDateTime.of(2021, Month.NOVEMBER, 20, 0, 0, 0),
				BigDecimal.valueOf(2000.0));
		
		DepotDelivery depotDelivery3 = graphGenerator.createDepotDelivery(
				depot2,
				LocalDateTime.of(2021, Month.NOVEMBER, 16, 0, 0, 0),
				BigDecimal.valueOf(3000.0));
		
		graphGenerator.createDepotDelivery(
				depot2,
				LocalDateTime.of(2021, Month.NOVEMBER, 21, 0, 0, 0),
				BigDecimal.valueOf(4000.0));
		
		flushAndClear();
		
		List<DepotDelivery> result = repository.findAllDeliveries(
				Set.of(depot1.getId(), depot2.getId()),
				PageRequest.of(0, 2));
		
		Assertions.assertEquals(Set.of(depotDelivery1, depotDelivery3), new HashSet<>(result));
	}
	
	@Test
	void testFindFirstDelivery() {
		Depot depot1 = graphGenerator.createDepot();
		Depot depot2 = graphGenerator.createDepot();
		
		DepotDelivery depotDelivery0 = graphGenerator.createDepotDelivery(
				depot1,
				LocalDateTime.of(2021, Month.NOVEMBER, 14, 0, 0, 0),
				BigDecimal.valueOf(500.0));
		depotDelivery0.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depotDelivery0);
		
		DepotDelivery depotDelivery1 = graphGenerator.createDepotDelivery(
				depot1,
				LocalDateTime.of(2021, Month.NOVEMBER, 15, 0, 0, 0),
				BigDecimal.valueOf(1000.0));
		
		graphGenerator.createDepotDelivery(
				depot1,
				LocalDateTime.of(2021, Month.NOVEMBER, 20, 0, 0, 0),
				BigDecimal.valueOf(2000.0));
		
		graphGenerator.createDepotDelivery(
				depot2,
				LocalDateTime.of(2021, Month.NOVEMBER, 16, 0, 0, 0),
				BigDecimal.valueOf(3000.0));
		
		graphGenerator.createDepotDelivery(
				depot2,
				LocalDateTime.of(2021, Month.NOVEMBER, 21, 0, 0, 0),
				BigDecimal.valueOf(4000.0));
		
		flushAndClear();
		
		Optional<DepotDelivery> result = repository.findFirstDelivery(Set.of(depot1.getId(), depot2.getId()));
		
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(depotDelivery1, result.get());
	}
}