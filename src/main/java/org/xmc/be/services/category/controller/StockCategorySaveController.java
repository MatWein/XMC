package org.xmc.be.services.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.StockCategory;
import org.xmc.be.repositories.category.StockCategoryJpaRepository;
import org.xmc.be.services.category.mapper.DtoStockCategoryToStockCategoryMapper;
import org.xmc.common.stubs.category.DtoStockCategory;

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
