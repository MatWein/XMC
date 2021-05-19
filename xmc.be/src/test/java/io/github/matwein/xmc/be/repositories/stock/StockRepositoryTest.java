package io.github.matwein.xmc.be.repositories.stock;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.Stock;
import io.github.matwein.xmc.be.entities.depot.StockCategory;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.stocks.DtoMinimalStock;
import io.github.matwein.xmc.common.stubs.stocks.DtoStockOverview;
import io.github.matwein.xmc.common.stubs.stocks.StockOverviewFields;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class StockRepositoryTest extends IntegrationTest {
	@Autowired
	private StockRepository repository;
	
	@Test
	void testLoadOverview() {
		PagingParams<StockOverviewFields> pagingParams = new PagingParams<>();
		pagingParams.setFilter("daimler");
		pagingParams.setOffset(1);
		pagingParams.setLimit(1);
		pagingParams.setSortBy(StockOverviewFields.WKN);
		pagingParams.setOrder(Order.DESC);
		
		StockCategory category = graphGenerator.createStockCategory("category");
		
		graphGenerator.createStock("ISIN1", "WKN1", "microsoft");
		graphGenerator.createStock("ISIN2", "WKN2", "daimler 1", category);
		Stock stock = graphGenerator.createStock("ISIN3", "WKN3", "daimler 2", category);
		graphGenerator.createStock("ISIN4", "WKN4", "daimler 3", category);
		graphGenerator.createStock("ISIN5", "WKN5", "apple");
		
		flushAndClear();
		
		QueryResults<DtoStockOverview> result = repository.loadOverview(pagingParams);
		
		Assertions.assertEquals(3, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		Assertions.assertEquals(stock.getId(), result.getResults().get(0).getId());
	}
	
	@Test
	void testLoadOverview_CheckAllSortFields() {
		graphGenerator.createStock("ISIN1", "WKN1", "microsoft");
		graphGenerator.createStock("ISIN2", "WKN2", "daimler 1");
		graphGenerator.createStock("ISIN3", "WKN3", "daimler 2");
		graphGenerator.createStock("ISIN4", "WKN4", "daimler 3");
		graphGenerator.createStock("ISIN5", "WKN5", "apple");
		
		flushAndClear();
		
		for (StockOverviewFields field : StockOverviewFields.values()) {
			QueryResults<DtoStockOverview> result = repository.loadOverview(new PagingParams<>(0, 10, field, Order.ASC, null));
			
			Assertions.assertEquals(5, result.getTotal());
			Assertions.assertEquals(5, result.getResults().size());
		}
	}
	
	@Test
	void testLoadOverview_CheckFields() {
		PagingParams<StockOverviewFields> pagingParams = new PagingParams<>();
		
		StockCategory category = graphGenerator.createStockCategory("category");
		
		Stock stock = graphGenerator.createStock("ISIN3", "WKN3", "daimler 2", category);
		
		flushAndClear();
		
		QueryResults<DtoStockOverview> result = repository.loadOverview(pagingParams);
		
		Assertions.assertEquals(1, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		
		var dtoStockOverview = result.getResults().get(0);
		Assertions.assertEquals(stock.getId(), dtoStockOverview.getId());
		Assertions.assertEquals(stock.getCreationDate(), dtoStockOverview.getCreationDate());
		Assertions.assertEquals(stock.getIsin(), dtoStockOverview.getIsin());
		Assertions.assertEquals(stock.getName(), dtoStockOverview.getName());
		Assertions.assertEquals(stock.getWkn(), dtoStockOverview.getWkn());
		Assertions.assertEquals(category.getId(), dtoStockOverview.getStockCategory().getId());
		Assertions.assertEquals(category.getName(), dtoStockOverview.getStockCategory().getName());
	}
	
	@Test
	void testLoadOverview_CheckFields_NoCategory() {
		PagingParams<StockOverviewFields> pagingParams = new PagingParams<>();
		
		Stock stock = graphGenerator.createStock("ISIN3", "WKN3", "daimler 2");
		stock.setStockCategory(null);
		session().saveOrUpdate(stock);
		
		flushAndClear();
		
		QueryResults<DtoStockOverview> result = repository.loadOverview(pagingParams);
		
		Assertions.assertEquals(1, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		
		var dtoStockOverview = result.getResults().get(0);
		Assertions.assertEquals(stock.getId(), dtoStockOverview.getId());
		Assertions.assertEquals(stock.getCreationDate(), dtoStockOverview.getCreationDate());
		Assertions.assertEquals(stock.getIsin(), dtoStockOverview.getIsin());
		Assertions.assertEquals(stock.getName(), dtoStockOverview.getName());
		Assertions.assertEquals(stock.getWkn(), dtoStockOverview.getWkn());
		Assertions.assertNull(dtoStockOverview.getStockCategory());
	}
	
	@Test
	void testLoadAllStocks() {
		graphGenerator.createStock("ISIN1", "WKN1", "microsoft");
		graphGenerator.createStock("ISIN2", "WKN2", "daimler 1");
		graphGenerator.createStock("ISIN3", "WKN3", "daimler 2");
		graphGenerator.createStock("ISIN4", "WKN4", "daimler 3");
		graphGenerator.createStock("ISIN5", "WKN5", "apple");
		
		flushAndClear();
		
		List<DtoMinimalStock> result = repository.loadAllStocks("daimler", 2);
		
		Assertions.assertEquals(2, result.size());
		Assertions.assertEquals("ISIN2", result.get(0).getIsin());
		Assertions.assertEquals("daimler 1", result.get(0).getName());
		Assertions.assertEquals("WKN2", result.get(0).getWkn());
		Assertions.assertEquals("ISIN3", result.get(1).getIsin());
		Assertions.assertEquals("daimler 2", result.get(1).getName());
		Assertions.assertEquals("WKN3", result.get(1).getWkn());
	}
}