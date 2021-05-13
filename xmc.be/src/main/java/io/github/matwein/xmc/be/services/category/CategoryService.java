package io.github.matwein.xmc.be.services.category;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.be.entities.cashaccount.Category;
import io.github.matwein.xmc.be.repositories.category.CategoryJpaRepository;
import io.github.matwein.xmc.be.repositories.category.CategoryRepository;
import io.github.matwein.xmc.be.services.category.controller.CategorySaveController;
import io.github.matwein.xmc.be.services.category.mapper.CategoryToDtoCategoryMapper;
import io.github.matwein.xmc.common.services.category.ICategoryService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.category.CategoryOverviewFields;
import io.github.matwein.xmc.common.stubs.category.DtoCategory;
import io.github.matwein.xmc.common.stubs.category.DtoCategoryOverview;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService implements ICategoryService {
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

    @Override
    public List<DtoCategory> loadAllCategories(IAsyncMonitor monitor) {
        LOGGER.info("Loading all available categories.");
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_ALL_CATEGORIES));

        return categoryJpaRepository.findByDeletionDateIsNull().stream()
                .map(categoryToDtoCategoryMapper)
                .collect(Collectors.toList());
    }
	
	@Override
    public void saveOrUpdate(IAsyncMonitor monitor, DtoCategory dtoCategory) {
        LOGGER.info("Saving category: {}", dtoCategory);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_CATEGORY));

        categorySaveController.saveOrUpdate(dtoCategory);
    }
	
	@Override
    public QueryResults<DtoCategoryOverview> loadOverview(IAsyncMonitor monitor, PagingParams<CategoryOverviewFields> pagingParams) {
        LOGGER.info("Loading category overview: {}", pagingParams);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_CATEGORY_OVERVIEW));

        return categoryRepository.loadOverview(pagingParams);
    }
	
	@Override
    public void markAsDeleted(IAsyncMonitor monitor, Long categoryId) {
        LOGGER.info("Marking category '{}' as deleted.", categoryId);
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_DELETE_CATEGORY));

        Category category = categoryJpaRepository.getOne(categoryId);
        category.setDeletionDate(LocalDateTime.now());
        categoryJpaRepository.save(category);
    }
}
