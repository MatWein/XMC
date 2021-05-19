package io.github.matwein.xmc.be.repositories.depot;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.depot.DepotOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.DtoDepotOverview;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

class DepotRepositoryTest extends IntegrationTest {
	@Autowired
	private DepotRepository repository;
	
	@Test
	void testLoadOverview() {
		PagingParams<DepotOverviewFields> pagingParams = new PagingParams<>();
		pagingParams.setOffset(0);
		pagingParams.setLimit(1);
		pagingParams.setSortBy(DepotOverviewFields.BANK_BLZ);
		pagingParams.setOrder(Order.ASC);
		
		Bank bank1 = graphGenerator.createBank();
		bank1.setBlz("111111");
		session().saveOrUpdate(bank1);
		
		Bank bank2 = graphGenerator.createBank();
		bank2.setBlz("000000");
		session().saveOrUpdate(bank2);
		
		graphGenerator.createDepot();
		
		graphGenerator.createDepot(bank1);
		
		Depot depot2 = graphGenerator.createDepot(bank2);
		
		Depot depot3 = graphGenerator.createDepot(bank2);
		depot3.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depot3);
		
		graphGenerator.createDepot();
		
		flushAndClear();
		
		QueryResults<DtoDepotOverview> result = repository.loadOverview(pagingParams);
		
		Assertions.assertEquals(4, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		Assertions.assertEquals(depot2.getId(), result.getResults().get(0).getId());
	}
	
	@Test
	void testLoadOverview_CheckAllSortFields() {
		Bank bank1 = graphGenerator.createBank();
		bank1.setBlz("111111");
		session().saveOrUpdate(bank1);
		
		Bank bank2 = graphGenerator.createBank();
		bank2.setBlz("000000");
		session().saveOrUpdate(bank2);
		
		graphGenerator.createDepot();
		
		graphGenerator.createDepot(bank1);
		
		graphGenerator.createDepot(bank2);
		
		Depot depot3 = graphGenerator.createDepot(bank2);
		depot3.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depot3);
		
		graphGenerator.createDepot();
		
		flushAndClear();
		
		for (DepotOverviewFields field : DepotOverviewFields.values()) {
			QueryResults<DtoDepotOverview> result = repository.loadOverview(new PagingParams<>(0, 10, field, Order.DESC, null));
			
			Assertions.assertEquals(4, result.getTotal());
			Assertions.assertEquals(4, result.getResults().size());
		}
	}
	
	@Test
	void testLoadOverview_CheckFields() {
		PagingParams<DepotOverviewFields> pagingParams = new PagingParams<>();
		pagingParams.setOffset(0);
		pagingParams.setLimit(1);
		
		byte[] logoBytes = "logo".getBytes();
		
		Bank bank = graphGenerator.createBank();
		bank.setBlz("BLZ");
		bank.setBic("BIC");
		bank.setLogo(graphGenerator.createBinaryData(logoBytes));
		bank.setName("NAME");
		session().saveOrUpdate(bank);
		
		Depot depot = graphGenerator.createDepot(bank);
		depot.setColor("#111111");
		depot.setName("DepotName");
		depot.setNumber("DepotNumber");
		session().saveOrUpdate(depot);
		
		DepotDelivery depotDelivery = graphGenerator.createDepotDelivery(depot);
		depotDelivery.setSaldo(BigDecimal.valueOf(100.0));
		depotDelivery.setDeliveryDate(LocalDateTime.of(2021, Month.MAY, 12, 0, 0, 0));
		session().saveOrUpdate(depotDelivery);
		
		depot.setLastDelivery(depotDelivery);
		session().saveOrUpdate(depot);
		
		flushAndClear();
		
		QueryResults<DtoDepotOverview> result = repository.loadOverview(pagingParams);
		
		Assertions.assertEquals(1, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		
		var dtoDepotOverview = result.getResults().get(0);
		Assertions.assertEquals(depot.getId(), dtoDepotOverview.getId());
		Assertions.assertEquals(bank.getBic(), dtoDepotOverview.getBank().getBic());
		Assertions.assertEquals(bank.getBlz(), dtoDepotOverview.getBank().getBlz());
		Assertions.assertEquals(bank.getId(), dtoDepotOverview.getBank().getId());
		Assertions.assertArrayEquals(logoBytes, dtoDepotOverview.getBank().getLogo());
		Assertions.assertEquals(bank.getName(), dtoDepotOverview.getBank().getName());
		Assertions.assertEquals(depot.getColor(), dtoDepotOverview.getColor());
		Assertions.assertEquals(depot.getCreationDate(), dtoDepotOverview.getCreationDate());
		Assertions.assertEquals(depotDelivery.getSaldo(), dtoDepotOverview.getLastSaldo().getValue());
		Assertions.assertEquals("EUR", dtoDepotOverview.getLastSaldo().getCurrency());
		Assertions.assertEquals(depotDelivery.getDeliveryDate(), dtoDepotOverview.getLastSaldoDate());
		Assertions.assertEquals(depot.getName(), dtoDepotOverview.getName());
		Assertions.assertEquals(depot.getNumber(), dtoDepotOverview.getNumber());
	}
}