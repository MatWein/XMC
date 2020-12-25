package org.xmc.be.services.category.mapper;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.BinaryData;
import org.xmc.be.entities.cashaccount.Category;
import org.xmc.common.stubs.category.DtoCategory;

import java.util.function.Function;

@Component
public class CategoryToDtoCategoryMapper implements Function<Category, DtoCategory> {
    @Override
    public DtoCategory apply(Category category) {
        var dtoCategory = new DtoCategory();

        dtoCategory.setId(category.getId());
        dtoCategory.setName(category.getName());

        BinaryData icon = category.getIcon();
        if (icon != null) {
            dtoCategory.setIcon(icon.getRawData());
        }

        return dtoCategory;
    }
}
