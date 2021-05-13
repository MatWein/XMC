package io.github.matwein.xmc.be.services.category.controller;

import io.github.matwein.xmc.be.entities.depot.StockCategory;
import io.github.matwein.xmc.be.repositories.category.StockCategoryJpaRepository;
import io.github.matwein.xmc.be.services.category.mapper.DtoStockCategoryToStockCategoryMapper;
import io.github.matwein.xmc.common.stubs.category.DtoStockCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockCategorySaveController {
    private final StockCategoryJpaRepository stockCategoryJpaRepository;
    private final DtoStockCategoryToStockCategoryMapper dtoStockCategoryToStockCategoryMapper;

    @Autowired
    public StockCategorySaveController(
            StockCategoryJpaRepository stockCategoryJpaRepository,
            DtoStockCategoryToStockCategoryMapper dtoStockCategoryToStockCategoryMapper) {

        this.stockCategoryJpaRepository = stockCategoryJpaRepository;
        this.dtoStockCategoryToStockCategoryMapper = dtoStockCategoryToStockCategoryMapper;
    }

    public void saveOrUpdate(DtoStockCategory dtoCategory) {
        StockCategory category = createOrUpdateCategory(dtoCategory);
	    stockCategoryJpaRepository.save(category);
    }

    private StockCategory createOrUpdateCategory(DtoStockCategory dtoCategory) {
        if (dtoCategory.getId() == null) {
            return dtoStockCategoryToStockCategoryMapper.map(dtoCategory);
        } else {
	        StockCategory category = stockCategoryJpaRepository.getOne(dtoCategory.getId());
	        dtoStockCategoryToStockCategoryMapper.update(category, dtoCategory);
            return category;
        }
    }
}
