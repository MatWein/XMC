package io.github.matwein.xmc.be.services.category;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.entities.cashaccount.Category;
import io.github.matwein.xmc.be.repositories.category.CategoryJpaRepository;
import io.github.matwein.xmc.be.repositories.category.CategoryRepository;
import io.github.matwein.xmc.be.services.category.controller.CategorySaveController;
import io.github.matwein.xmc.common.services.category.ICategoryService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.category.CategoryOverviewFields;
import io.github.matwein.xmc.common.stubs.category.DtoCategory;
import io.github.matwein.xmc.common.stubs.category.DtoCategoryOverview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CategoryService implements ICategoryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
	
	private static final PagingParams<CategoryOverviewFields> PAGING_PARAMS_ALL_CATEGORIES = new PagingParams<>(0, Integer.MAX_VALUE, CategoryOverviewFields.NAME, Order.ASC, null);
	
	private final CategoryJpaRepository categoryJpaRepository;
    private final CategorySaveController categorySaveController;
    private final CategoryRepository categoryRepository;
	
	@Autowired
    public CategoryService(
		    CategoryJpaRepository categoryJpaRepository,
		    CategorySaveController categorySaveController,
		    CategoryRepository categoryRepository) {

        this.categoryJpaRepository = categoryJpaRepository;
        this.categorySaveController = categorySaveController;
        this.categoryRepository = categoryRepository;
	}

    @Override
    public List<DtoCategoryOverview> loadAllCategories(IAsyncMonitor monitor) {
        LOGGER.info("Loading all available categories.");
        monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_ALL_CATEGORIES));
	
	    return categoryRepository.loadOverview(PAGING_PARAMS_ALL_CATEGORIES).getResults();
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

        Category category = categoryJpaRepository.getReferenceById(categoryId);
        category.setDeletionDate(LocalDateTime.now());
        categoryJpaRepository.save(category);
    }
}
