package io.github.matwein.xmc.be.repositories.depot;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.be.entities.depot.DepotItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class DepotItemJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private DepotItemJpaRepository repository;
	
	@Test
	void testFindByDeliveryAndDeletionDateIsNull() {
		DepotDelivery delivery = graphGenerator.createDepotDelivery();
		
		flushAndClear();
		
		List<DepotItem> result = repository.findByDeliveryAndDeletionDateIsNull(delivery);
		
		
	}
}