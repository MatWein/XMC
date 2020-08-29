package org.xmc.be.repositories.category;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.be.entities.Category;

import java.time.LocalDateTime;
import java.util.List;

class CategoryJpaRepositoryTest extends IntegrationTest {
    @Autowired
    private CategoryJpaRepository repository;

    @Test
    void testFindByDeletionDateIsNull() {
        Category expectedResult = graphGenerator.createCategory();

        Category deletedCategory = graphGenerator.createCategory();
        deletedCategory.setDeletionDate(LocalDateTime.now());
        session().saveOrUpdate(deletedCategory);

        flush();

        List<Category> result = repository.findByDeletionDateIsNull();

        Assert.assertEquals(Lists.newArrayList(expectedResult), result);
    }
}