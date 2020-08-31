package org.xmc.be.repositories.category;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.be.entities.Category;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.category.CategoryOverviewFields;
import org.xmc.common.stubs.category.DtoCategoryOverview;

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

        flush();

        QueryResults<DtoCategoryOverview> result = repository.loadOverview(pagingParams);

        Assert.assertEquals(3, result.getTotal());
        Assert.assertEquals(2, result.getResults().size());
        Assert.assertEquals(category1.getId(), result.getResults().get(0).getId());
        Assert.assertEquals(category2.getId(), result.getResults().get(1).getId());
    }
}