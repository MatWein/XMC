package io.github.matwein.xmc.be.repositories.category;

import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.depot.StockCategory;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.category.DtoStockCategoryOverview;
import io.github.matwein.xmc.common.stubs.category.StockCategoryOverviewFields;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

class StockCategoryRepositoryTest extends IntegrationTest {
	@Autowired
	private StockCategoryRepository repository;
	
	@Test
	void testLoadOverview() {
		PagingParams<StockCategoryOverviewFields> pagingParams = new PagingParams<>();
		pagingParams.setSortBy(StockCategoryOverviewFields.NAME);
		pagingParams.setOrder(Order.ASC);
		pagingParams.setOffset(0);
		pagingParams.setLimit(2);
		
		StockCategory deletedCategory = graphGenerator.createStockCategory();
		deletedCategory.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(deletedCategory);
		
		StockCategory category1 = graphGenerator.createStockCategory();
		category1.setName("Cat1");
		session().saveOrUpdate(category1);
		
		StockCategory category2 = graphGenerator.createStockCategory();
		category2.setName("Cat2");
		session().saveOrUpdate(category2);
		
		StockCategory category3 = graphGenerator.createStockCategory();
		category3.setName("Cat3");
		session().saveOrUpdate(category3);
		
		flushAndClear();
		
		QueryResults<DtoStockCategoryOverview> result = repository.loadOverview(pagingParams);
		
		Assertions.assertEquals(3, result.getTotal());
		Assertions.assertEquals(2, result.getResults().size());
		Assertions.assertEquals(category1.getId(), result.getResults().get(0).getId());
		Assertions.assertEquals(category2.getId(), result.getResults().get(1).getId());
	}
	
	@Test
	void testLoadOverview_CheckAllSortFields() {
		StockCategory deletedCategory = graphGenerator.createStockCategory();
		deletedCategory.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(deletedCategory);
		
		StockCategory category1 = graphGenerator.createStockCategory();
		category1.setName("Cat1");
		session().saveOrUpdate(category1);
		
		StockCategory category2 = graphGenerator.createStockCategory();
		category2.setName("Cat2");
		session().saveOrUpdate(category2);
		
		StockCategory category3 = graphGenerator.createStockCategory();
		category3.setName("Cat3");
		session().saveOrUpdate(category3);
		
		flushAndClear();
		
		for (StockCategoryOverviewFields field : StockCategoryOverviewFields.values()) {
			QueryResults<DtoStockCategoryOverview> result = repository.loadOverview(new PagingParams<>(0, 10, field, Order.DESC, null));
			
			Assertions.assertEquals(3, result.getTotal());
			Assertions.assertEquals(3, result.getResults().size());
		}
	}
	
	@Test
	void testLoadOverview_CheckFields() {
		PagingParams<StockCategoryOverviewFields> pagingParams = new PagingParams<>();
		pagingParams.setSortBy(StockCategoryOverviewFields.CREATION_DATE);
		pagingParams.setOrder(Order.ASC);
		pagingParams.setOffset(0);
		pagingParams.setLimit(2);
		pagingParams.setFilter("Kate");
		
		StockCategory category = graphGenerator.createStockCategory();
		category.setName("Some Kate");
		session().saveOrUpdate(category);
		
		flushAndClear();
		
		QueryResults<DtoStockCategoryOverview> result = repository.loadOverview(pagingParams);
		
		Assertions.assertEquals(1, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		
		var dtoCategoryOverview = result.getResults().get(0);
		Assertions.assertEquals(category.getId(), dtoCategoryOverview.getId());
		Assertions.assertEquals(category.getName(), dtoCategoryOverview.getName());
		Assertions.assertEquals(category.getCreationDate(), dtoCategoryOverview.getCreationDate());
	}
}