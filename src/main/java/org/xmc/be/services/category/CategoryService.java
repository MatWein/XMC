package org.xmc.be.services.category;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.Category;
import org.xmc.be.repositories.category.CategoryJpaRepository;
import org.xmc.be.repositories.category.CategoryRepository;
import org.xmc.be.services.category.controller.CategorySaveController;
import org.xmc.be.services.category.mapper.CategoryToDtoCategoryMapper;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.category.CategoryOverviewFields;
import org.xmc.common.stubs.category.DtoCategory;
import org.xmc.common.stubs.category.DtoCategoryOverview;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategorySaveController categorySaveController;
    private final CategoryRepository categoryRepository;
    private final CategoryToDtoCategoryMapper categoryToDtoCategoryMapper;

    @Autowired
    public CategoryService(
            CategoryJpaRepository categoryJpaRepository,
            CategorySaveController categorySaveController,
            CategoryRepository categoryRepository,
            CategoryToDtoCategoryMapper categoryToDtoCategoryMapper) {

        this.categoryJpaRepository = categoryJpaRepository;
        this.categorySaveController = categorySaveController;
        this.categoryRepository = categoryRepository;
        this.categoryToDtoCategoryMapper = categoryToDtoCategoryMapper;
    }

    public List<DtoCategory> loadAllCategories(AsyncMonitor monitor) {
        LOGGER.info("Loading all available categories.");
        monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_ALL_CATEGORIES);

        return categoryJpaRepository.findByDeletionDateIsNull().stream()
                .map(categoryToDtoCategoryMapper)
                .collect(Collectors.toList());
    }

    public void saveOrUpdate(AsyncMonitor monitor, DtoCategory dtoCategory) {
        LOGGER.info("Saving category: {}", dtoCategory);
        monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_CATEGORY);

        categorySaveController.saveOrUpdate(dtoCategory);
    }

    public QueryResults<DtoCategoryOverview> loadOverview(AsyncMonitor monitor, PagingParams<CategoryOverviewFields> pagingParams) {
        LOGGER.info("Loading category overview: {}", pagingParams);
        monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_CATEGORY_OVERVIEW);

        return categoryRepository.loadOverview(pagingParams);
    }

    public void markAsDeleted(AsyncMonitor monitor, Long categoryId) {
        LOGGER.info("Marking category '{}' as deleted.", categoryId);
        monitor.setStatusText(MessageKey.ASYNC_TASK_DELETE_CATEGORY);

        Category category = categoryJpaRepository.getOne(categoryId);
        category.setDeletionDate(LocalDateTime.now());
        categoryJpaRepository.save(category);
    }
}
