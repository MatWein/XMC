package org.xmc.be.services.category.mapper;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.StockCategory;
import org.xmc.common.stubs.category.DtoStockCategory;

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
