package io.github.matwein.xmc.be.repositories.depot;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.be.entities.depot.DepotItem;
import io.github.matwein.xmc.be.entities.depot.Stock;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemOverview;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class DepotItemRepositoryTest extends IntegrationTest {
	@Autowired
	private DepotItemRepository repository;
	
	@Test
	void testLoadOverview() {
		PagingParams<DepotItemOverviewFields> pagingParams = new PagingParams<>();
		pagingParams.setOffset(2);
		pagingParams.setLimit(2);
		pagingParams.setSortBy(DepotItemOverviewFields.AMOUNT);
		pagingParams.setOrder(Order.DESC);
		
		DepotDelivery depotDeliveryEntity = graphGenerator.createDepotDelivery();
		
		graphGenerator.createDepotItem();
		
		DepotItem depotItem1 = graphGenerator.createDepotItem(depotDeliveryEntity);
		depotItem1.setAmount(BigDecimal.valueOf(100.0));
		session().saveOrUpdate(depotItem1);
		
		DepotItem depotItem2 = graphGenerator.createDepotItem(depotDeliveryEntity);
		depotItem2.setAmount(BigDecimal.valueOf(200.0));
		session().saveOrUpdate(depotItem2);
		
		DepotItem depotItem3 = graphGenerator.createDepotItem(depotDeliveryEntity);
		depotItem3.setAmount(BigDecimal.valueOf(300.0));
		session().saveOrUpdate(depotItem3);
		
		DepotItem depotItem4 = graphGenerator.createDepotItem(depotDeliveryEntity);
		depotItem4.setAmount(BigDecimal.valueOf(400.0));
		session().saveOrUpdate(depotItem4);
		
		DepotItem depotItem5 = graphGenerator.createDepotItem(depotDeliveryEntity);
		depotItem5.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depotItem5);
		
		graphGenerator.createDepotItem();
		
		flushAndClear();
		
		QueryResults<DtoDepotItemOverview> result = repository.loadOverview(depotDeliveryEntity, pagingParams);
		
		Assertions.assertEquals(4, result.getTotal());
		Assertions.assertEquals(2, result.getResults().size());
		Assertions.assertEquals(depotItem2.getId(), result.getResults().get(0).getId());
		Assertions.assertEquals(depotItem1.getId(), result.getResults().get(1).getId());
	}
	
	@Test
	void testLoadOverview_CheckAllSortFields() {
		DepotDelivery depotDeliveryEntity = graphGenerator.createDepotDelivery();
		
		DepotItem depotItem1 = graphGenerator.createDepotItem(depotDeliveryEntity);
		depotItem1.setAmount(BigDecimal.valueOf(100.0));
		session().saveOrUpdate(depotItem1);
		
		DepotItem depotItem2 = graphGenerator.createDepotItem(depotDeliveryEntity);
		depotItem2.setAmount(BigDecimal.valueOf(200.0));
		session().saveOrUpdate(depotItem2);
		
		DepotItem depotItem3 = graphGenerator.createDepotItem(depotDeliveryEntity);
		depotItem3.setAmount(BigDecimal.valueOf(300.0));
		session().saveOrUpdate(depotItem3);
		
		DepotItem depotItem4 = graphGenerator.createDepotItem(depotDeliveryEntity);
		depotItem4.setAmount(BigDecimal.valueOf(400.0));
		session().saveOrUpdate(depotItem4);
		
		DepotItem depotItem5 = graphGenerator.createDepotItem(depotDeliveryEntity);
		depotItem5.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depotItem5);
		
		flushAndClear();
		
		for (DepotItemOverviewFields field : DepotItemOverviewFields.values()) {
			QueryResults<DtoDepotItemOverview> result = repository.loadOverview(
					depotDeliveryEntity,
					new PagingParams<>(0, 10, field, Order.ASC, null));
			
			Assertions.assertEquals(4, result.getTotal());
			Assertions.assertEquals(4, result.getResults().size());
		}
	}
	
	@Test
	void testLoadOverview_CheckFields() {
		PagingParams<DepotItemOverviewFields> pagingParams = new PagingParams<>();
		
		DepotDelivery depotDeliveryEntity = graphGenerator.createDepotDelivery();
		
		graphGenerator.createStock();
		Stock stock = graphGenerator.createStock("ISIN1", "WKN1", "NAME1");
		graphGenerator.createStock();
		
		DepotItem depotItem = graphGenerator.createDepotItem(depotDeliveryEntity);
		depotItem.setAmount(BigDecimal.valueOf(100.0));
		depotItem.setCourse(BigDecimal.valueOf(120.0));
		depotItem.setCurrency("EUR");
		depotItem.setIsin("ISIN1");
		depotItem.setValue(BigDecimal.valueOf(200.0));
		session().saveOrUpdate(depotItem);
		
		flushAndClear();
		
		QueryResults<DtoDepotItemOverview> result = repository.loadOverview(depotDeliveryEntity, pagingParams);
		
		Assertions.assertEquals(1, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		
		var dtoDepotItemOverview = result.getResults().get(0);
		Assertions.assertEquals(depotItem.getAmount(), dtoDepotItemOverview.getAmount());
		Assertions.assertEquals(depotItem.getCourse(), dtoDepotItemOverview.getCourse());
		Assertions.assertEquals(depotItem.getCreationDate(), dtoDepotItemOverview.getCreationDate());
		Assertions.assertEquals(depotItem.getCurrency(), dtoDepotItemOverview.getCurrency());
		Assertions.assertEquals(depotItem.getId(), dtoDepotItemOverview.getId());
		Assertions.assertEquals(depotItem.getIsin(), dtoDepotItemOverview.getIsin());
		Assertions.assertEquals(depotItem.getValue(), dtoDepotItemOverview.getValue());
		Assertions.assertEquals(stock.getName(), dtoDepotItemOverview.getName());
		Assertions.assertEquals(stock.getWkn(), dtoDepotItemOverview.getWkn());
	}
	
	@Test
	void testLoadOverview_CheckFields_UnknownStock() {
		PagingParams<DepotItemOverviewFields> pagingParams = new PagingParams<>();
		
		DepotDelivery depotDeliveryEntity = graphGenerator.createDepotDelivery();
		
		graphGenerator.createStock();
		graphGenerator.createStock();
		
		DepotItem depotItem = graphGenerator.createDepotItem(depotDeliveryEntity);
		depotItem.setAmount(BigDecimal.valueOf(100.0));
		depotItem.setCourse(BigDecimal.valueOf(120.0));
		depotItem.setCurrency("EUR");
		depotItem.setIsin("ISIN1");
		depotItem.setValue(BigDecimal.valueOf(200.0));
		session().saveOrUpdate(depotItem);
		
		flushAndClear();
		
		QueryResults<DtoDepotItemOverview> result = repository.loadOverview(depotDeliveryEntity, pagingParams);
		
		Assertions.assertEquals(1, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		
		var dtoDepotItemOverview = result.getResults().get(0);
		Assertions.assertEquals(depotItem.getAmount(), dtoDepotItemOverview.getAmount());
		Assertions.assertEquals(depotItem.getCourse(), dtoDepotItemOverview.getCourse());
		Assertions.assertEquals(depotItem.getCreationDate(), dtoDepotItemOverview.getCreationDate());
		Assertions.assertEquals(depotItem.getCurrency(), dtoDepotItemOverview.getCurrency());
		Assertions.assertEquals(depotItem.getId(), dtoDepotItemOverview.getId());
		Assertions.assertEquals(depotItem.getIsin(), dtoDepotItemOverview.getIsin());
		Assertions.assertEquals(depotItem.getValue(), dtoDepotItemOverview.getValue());
		Assertions.assertNull(dtoDepotItemOverview.getName());
		Assertions.assertNull(dtoDepotItemOverview.getWkn());
	}
}