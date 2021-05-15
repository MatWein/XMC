package io.github.matwein.xmc.be.repositories.category;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.BinaryData;
import io.github.matwein.xmc.be.entities.cashaccount.Category;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.category.CategoryOverviewFields;
import io.github.matwein.xmc.common.stubs.category.DtoCategoryOverview;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

class CategoryRepositoryTest extends IntegrationTest {
    @Autowired
    private CategoryRepository repository;

    @Test
    void testLoadOverview() {
        PagingParams<CategoryOverviewFields> pagingParams = new PagingParams<>();
        pagingParams.setSortBy(CategoryOverviewFields.NAME);
        pagingParams.setOrder(Order.ASC);
        pagingParams.setOffset(0);
        pagingParams.setLimit(2);

        Category deletedCategory = graphGenerator.createCategory();
        deletedCategory.setDeletionDate(LocalDateTime.now());
        session().saveOrUpdate(deletedCategory);

        Category category1 = graphGenerator.createCategory();
        category1.setName("Cat1");
        category1.setIcon(null);
        session().saveOrUpdate(category1);

        Category category2 = graphGenerator.createCategory();
        category2.setName("Cat2");
        category2.setIcon(graphGenerator.createBinaryData());
        session().saveOrUpdate(category2);

        Category category3 = graphGenerator.createCategory();
        category3.setName("Cat3");
        category3.setIcon(null);
        session().saveOrUpdate(category3);
	
	    flushAndClear();

        QueryResults<DtoCategoryOverview> result = repository.loadOverview(pagingParams);

        Assertions.assertEquals(3, result.getTotal());
        Assertions.assertEquals(2, result.getResults().size());
        Assertions.assertEquals(category1.getId(), result.getResults().get(0).getId());
        Assertions.assertEquals(category2.getId(), result.getResults().get(1).getId());
    }
	
	@Test
	void testLoadOverview_CheckAllSortFields() {
		Category deletedCategory = graphGenerator.createCategory();
		deletedCategory.setDeletionDate(LocalDateTime.now());
		session().saveOrUpdate(deletedCategory);
		
		Category category1 = graphGenerator.createCategory();
		category1.setName("Cat1");
		category1.setIcon(null);
		session().saveOrUpdate(category1);
		
		Category category2 = graphGenerator.createCategory();
		category2.setName("Cat2");
		category2.setIcon(graphGenerator.createBinaryData());
		session().saveOrUpdate(category2);
		
		Category category3 = graphGenerator.createCategory();
		category3.setName("Cat3");
		category3.setIcon(null);
		session().saveOrUpdate(category3);
		
		flushAndClear();
		
		for (CategoryOverviewFields field : CategoryOverviewFields.values()) {
			QueryResults<DtoCategoryOverview> result = repository.loadOverview(new PagingParams<>(0, 10, field, Order.DESC, null));
			
			Assertions.assertEquals(3, result.getTotal());
			Assertions.assertEquals(3, result.getResults().size());
		}
	}
	
	@Test
	void testLoadOverview_CheckFields() {
		PagingParams<CategoryOverviewFields> pagingParams = new PagingParams<>();
		pagingParams.setSortBy(CategoryOverviewFields.CREATION_DATE);
		pagingParams.setOrder(Order.ASC);
		pagingParams.setOffset(0);
		pagingParams.setLimit(2);
		pagingParams.setFilter("Kate");
		
		byte[] iconBytes = "icon".getBytes();
		BinaryData icon = graphGenerator.createBinaryData(iconBytes);
		
		Category category = graphGenerator.createCategory();
		category.setName("Some Kate");
		category.setIcon(icon);
		session().saveOrUpdate(category);
		
		flushAndClear();
		
		QueryResults<DtoCategoryOverview> result = repository.loadOverview(pagingParams);
		
		Assertions.assertEquals(1, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		
		var dtoCategoryOverview = result.getResults().get(0);
		Assertions.assertEquals(category.getId(), dtoCategoryOverview.getId());
		Assertions.assertEquals(category.getName(), dtoCategoryOverview.getName());
		Assertions.assertArrayEquals(iconBytes, dtoCategoryOverview.getIcon());
		Assertions.assertEquals(category.getCreationDate(), dtoCategoryOverview.getCreationDate());
	}
}