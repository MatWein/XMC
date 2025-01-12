package io.github.matwein.xmc.be.repositories.depot;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryOverview;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

class DepotDeliveryRepositoryTest extends IntegrationTest {
	@Autowired
	private DepotDeliveryRepository repository;
	
	@Test
	void testLoadOverview() {
		PagingParams<DepotDeliveryOverviewFields> pagingParams = new PagingParams<>();
		pagingParams.setOffset(0);
		pagingParams.setLimit(2);
		pagingParams.setOrder(Order.DESC);
		pagingParams.setSortBy(DepotDeliveryOverviewFields.SALDO);
		
		Depot depotEntity = graphGenerator.createDepot();
		
		DepotDelivery depotDelivery1 = graphGenerator.createDepotDelivery(depotEntity);
		depotDelivery1.setSaldo(BigDecimal.valueOf(2000.0));
		session().saveOrUpdate(depotDelivery1);
		
		DepotDelivery depotDelivery2 = graphGenerator.createDepotDelivery(depotEntity);
		depotDelivery2.setSaldo(BigDecimal.valueOf(3000.0));
		session().saveOrUpdate(depotDelivery2);
		
		DepotDelivery depotDelivery3 = graphGenerator.createDepotDelivery(depotEntity);
		depotDelivery3.setSaldo(BigDecimal.valueOf(4000.0));
		session().saveOrUpdate(depotDelivery3);
		
		DepotDelivery depotDelivery4 = graphGenerator.createDepotDelivery(depotEntity);
		depotDelivery4.setSaldo(BigDecimal.valueOf(4000.0));
		depotDelivery4.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depotDelivery4);
		
		DepotDelivery depotDelivery5 = graphGenerator.createDepotDelivery();
		depotDelivery5.setSaldo(BigDecimal.valueOf(5000.0));
		session().saveOrUpdate(depotDelivery5);
		
		flushAndClear();
		
		QueryResults<DtoDepotDeliveryOverview> result = repository.loadOverview(depotEntity, pagingParams);
		
		Assertions.assertEquals(3, result.getTotal());
		Assertions.assertEquals(2, result.getResults().size());
		Assertions.assertEquals(depotDelivery3.getId(), result.getResults().get(0).getId());
		Assertions.assertEquals(depotDelivery2.getId(), result.getResults().get(1).getId());
	}
	
	@Test
	void testLoadOverview_CheckAllSortFields() {
		Depot depotEntity = graphGenerator.createDepot();
		
		graphGenerator.createDepotDelivery();
		
		graphGenerator.createDepotDelivery(depotEntity);
		graphGenerator.createDepotDelivery(depotEntity);
		graphGenerator.createDepotDelivery(depotEntity);
		
		graphGenerator.createDepotDelivery();
		
		flushAndClear();
		
		for (DepotDeliveryOverviewFields field : DepotDeliveryOverviewFields.values()) {
			QueryResults<DtoDepotDeliveryOverview> result = repository.loadOverview(
					depotEntity,
					new PagingParams<>(0, 10, field, Order.ASC, null));
			
			Assertions.assertEquals(3, result.getTotal());
			Assertions.assertEquals(3, result.getResults().size());
		}
	}
	
	@Test
	void testLoadOverview_CheckFields() {
		PagingParams<DepotDeliveryOverviewFields> pagingParams = new PagingParams<>();
		
		Depot depotEntity = graphGenerator.createDepot();
		
		DepotDelivery depotDelivery = graphGenerator.createDepotDelivery(depotEntity);
		depotDelivery.setDeliveryDate(LocalDateTime.of(2021, Month.MAY, 22, 0, 0, 0));
		depotDelivery.setSaldo(BigDecimal.valueOf(2000.0));
		session().saveOrUpdate(depotDelivery);
		
		flushAndClear();
		
		QueryResults<DtoDepotDeliveryOverview> result = repository.loadOverview(depotEntity, pagingParams);
		
		Assertions.assertEquals(1, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		
		var dtoDepotDeliveryOverview = result.getResults().get(0);
		Assertions.assertEquals(depotDelivery.getCreationDate(), dtoDepotDeliveryOverview.getCreationDate());
		Assertions.assertEquals(depotDelivery.getDeliveryDate(), dtoDepotDeliveryOverview.getDeliveryDate());
		Assertions.assertEquals(depotDelivery.getSaldo(), dtoDepotDeliveryOverview.getSaldo().getValue());
		Assertions.assertEquals("EUR", dtoDepotDeliveryOverview.getSaldo().getCurrency());
	}
	
	@Test
	void testLoadMostRecentDeliveryOfDepot() {
		Depot depotEntity = graphGenerator.createDepot();
		
		DepotDelivery depotDelivery1 = graphGenerator.createDepotDelivery(depotEntity);
		depotDelivery1.setDeliveryDate(LocalDateTime.of(2021, Month.MAY, 22, 0, 0, 0));
		session().saveOrUpdate(depotDelivery1);
		
		DepotDelivery depotDelivery2 = graphGenerator.createDepotDelivery(depotEntity);
		depotDelivery2.setDeliveryDate(LocalDateTime.of(2021, Month.MAY, 25, 0, 0, 0));
		session().saveOrUpdate(depotDelivery2);
		
		flushAndClear();
		
		Optional<DepotDelivery> result = repository.loadMostRecentDeliveryOfDepot(depotEntity);
		
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(depotDelivery2, result.get());
	}
	
	@Test
	void testLoadMostRecentDeliveryOfDepot_NotFound() {
		Depot depotEntity = graphGenerator.createDepot();
		
		flushAndClear();
		
		Optional<DepotDelivery> result = repository.loadMostRecentDeliveryOfDepot(depotEntity);
		
		Assertions.assertFalse(result.isPresent());
	}
}