package io.github.matwein.xmc.be.services.category.mapper;

import io.github.matwein.xmc.be.entities.depot.StockCategory;
import io.github.matwein.xmc.common.stubs.category.DtoStockCategory;
import org.springframework.stereotype.Component;

@Component
public class DtoStockCategoryToStockCategoryMapper {
    public StockCategory map(DtoStockCategory dtoCategory) {
	    StockCategory category = new StockCategory();
        update(category, dtoCategory);
        return category;
    }

    public void update(StockCategory category, DtoStockCategory dtoCategory) {
        category.setName(dtoCategory.getName());
    }
}
