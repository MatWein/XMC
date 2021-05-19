package io.github.matwein.xmc.be.repositories.depot;

import com.google.common.collect.Sets;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.Depot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

class DepotJpaRepositoryTest extends IntegrationTest {
	@Autowired
	private DepotJpaRepository repository;
	
	@Test
	void testFindByDeletionDateIsNull() {
		Depot depot1 = graphGenerator.createDepot();
		
		Depot depot2 = graphGenerator.createDepot();
		depot2.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depot2);
		
		Depot depot3 = graphGenerator.createDepot();
		
		flushAndClear();
		
		List<Depot> result = repository.findByDeletionDateIsNull();
		
		Assertions.assertEquals(Sets.newHashSet(depot1, depot3), Sets.newHashSet(result));
	}
}