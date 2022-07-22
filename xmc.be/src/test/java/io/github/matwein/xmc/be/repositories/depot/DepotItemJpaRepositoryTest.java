package io.github.matwein.xmc.be.repositories.depot;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.be.entities.depot.DepotItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

class DepotItemJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private DepotItemJpaRepository repository;
	
	@Test
	void testFindByDeliveryAndDeletionDateIsNull() {
		DepotDelivery delivery = graphGenerator.createDepotDelivery();
		
		DepotItem depotItem1 = graphGenerator.createDepotItem(delivery);
		
		DepotItem depotItem2 = graphGenerator.createDepotItem(delivery);
		depotItem2.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depotItem2);
		
		flushAndClear();
		
		List<DepotItem> result = repository.findByDeliveryAndDeletionDateIsNull(delivery);
		
		Assertions.assertEquals(List.of(depotItem1), result);
	}
}