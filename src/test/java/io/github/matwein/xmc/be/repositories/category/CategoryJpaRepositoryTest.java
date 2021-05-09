package io.github.matwein.xmc.be.repositories.category;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.cashaccount.Category;

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

        Assertions.assertEquals(Lists.newArrayList(expectedResult), result);
    }
}