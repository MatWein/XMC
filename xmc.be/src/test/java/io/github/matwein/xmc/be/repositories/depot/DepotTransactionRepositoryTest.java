package io.github.matwein.xmc.be.repositories.depot;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotTransaction;
import io.github.matwein.xmc.be.entities.depot.Stock;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransactionOverview;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

class DepotTransactionRepositoryTest extends IntegrationTest {
	@Autowired
	private DepotTransactionRepository repository;
	
	@Test
	void testLoadOverview() {
		PagingParams<DepotTransactionOverviewFields> pagingParams = new PagingParams<>();
		pagingParams.setOffset(1);
		pagingParams.setLimit(1);
		pagingParams.setOrder(Order.DESC);
		pagingParams.setSortBy(DepotTransactionOverviewFields.WKN);
		
		Depot depot = graphGenerator.createDepot();
		
		graphGenerator.createStock("ISIN1", "111111", "NAME1");
		graphGenerator.createStock("ISIN2", "222222", "NAME2");
		graphGenerator.createStock("ISIN3", "333333", "NAME3");
		
		graphGenerator.createDepotTransaction();
		
		DepotTransaction depotTransaction1 = graphGenerator.createDepotTransaction(depot);
		depotTransaction1.setIsin("ISIN1");
		session().saveOrUpdate(depotTransaction1);
		
		DepotTransaction depotTransaction2 = graphGenerator.createDepotTransaction(depot);
		depotTransaction2.setIsin("ISIN2");
		session().saveOrUpdate(depotTransaction2);
		
		DepotTransaction depotTransaction3 = graphGenerator.createDepotTransaction(depot);
		depotTransaction3.setIsin("ISIN3");
		session().saveOrUpdate(depotTransaction3);
		
		DepotTransaction depotTransaction4 = graphGenerator.createDepotTransaction(depot);
		depotTransaction4.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depotTransaction4);
		
		graphGenerator.createDepotTransaction();
		
		flushAndClear();
		
		QueryResults<DtoDepotTransactionOverview> result = repository.loadOverview(depot, pagingParams);
		
		Assertions.assertEquals(3, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		Assertions.assertEquals(depotTransaction2.getId(), result.getResults().get(0).getId());
	}
	
	@Test
	void testLoadOverview_CheckAllSortFields() {
		Depot depot = graphGenerator.createDepot();
		
		graphGenerator.createDepotTransaction();
		
		DepotTransaction depotTransaction1 = graphGenerator.createDepotTransaction(depot);
		depotTransaction1.setIsin("ISIN1");
		session().saveOrUpdate(depotTransaction1);
		
		DepotTransaction depotTransaction2 = graphGenerator.createDepotTransaction(depot);
		depotTransaction2.setIsin("ISIN2");
		session().saveOrUpdate(depotTransaction2);
		
		DepotTransaction depotTransaction3 = graphGenerator.createDepotTransaction(depot);
		depotTransaction3.setIsin("ISIN3");
		session().saveOrUpdate(depotTransaction3);
		
		DepotTransaction depotTransaction4 = graphGenerator.createDepotTransaction(depot);
		depotTransaction4.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(depotTransaction4);
		
		graphGenerator.createDepotTransaction();
		
		flushAndClear();
		
		for (DepotTransactionOverviewFields field : DepotTransactionOverviewFields.values()) {
			QueryResults<DtoDepotTransactionOverview> result = repository.loadOverview(depot, new PagingParams<>(0, 10, field, Order.ASC, "ISIN"));
			
			Assertions.assertEquals(3, result.getTotal());
			Assertions.assertEquals(3, result.getResults().size());
		}
	}
	
	@Test
	void testLoadOverview_CheckFields() {
		PagingParams<DepotTransactionOverviewFields> pagingParams = new PagingParams<>();
		
		Depot depot = graphGenerator.createDepot();
		
		Stock stock = graphGenerator.createStock("ISIN1", "111111", "NAME1");
		
		DepotTransaction depotTransaction = graphGenerator.createDepotTransaction(depot);
		depotTransaction.setIsin("ISIN1");
		depotTransaction.setAmount(BigDecimal.valueOf(100.0));
		depotTransaction.setCourse(BigDecimal.valueOf(200.0));
		depotTransaction.setCurrency("AUD");
		depotTransaction.setDescription("some description");
		depotTransaction.setValue(BigDecimal.valueOf(300.0));
		depotTransaction.setValutaDate(LocalDate.of(2021, Month.OCTOBER, 10));
		session().saveOrUpdate(depotTransaction);
		
		flushAndClear();
		
		QueryResults<DtoDepotTransactionOverview> result = repository.loadOverview(depot, pagingParams);
		
		Assertions.assertEquals(1, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		
		var dtoDepotTransactionOverview = result.getResults().get(0);
		Assertions.assertEquals(depotTransaction.getId(), dtoDepotTransactionOverview.getId());
		Assertions.assertEquals(depotTransaction.getAmount(), dtoDepotTransactionOverview.getAmount());
		Assertions.assertEquals(depotTransaction.getCourse(), dtoDepotTransactionOverview.getCourse());
		Assertions.assertEquals(depotTransaction.getCreationDate(), dtoDepotTransactionOverview.getCreationDate());
		Assertions.assertEquals(depotTransaction.getCurrency(), dtoDepotTransactionOverview.getCurrency());
		Assertions.assertEquals(depotTransaction.getDescription(), dtoDepotTransactionOverview.getDescription());
		Assertions.assertEquals(depotTransaction.getIsin(), dtoDepotTransactionOverview.getIsin());
		Assertions.assertEquals(stock.getName(), dtoDepotTransactionOverview.getName());
		Assertions.assertEquals(depotTransaction.getValue(), dtoDepotTransactionOverview.getValue());
		Assertions.assertEquals(depotTransaction.getValutaDate(), dtoDepotTransactionOverview.getValutaDate());
		Assertions.assertEquals(stock.getWkn(), dtoDepotTransactionOverview.getWkn());
	}
}