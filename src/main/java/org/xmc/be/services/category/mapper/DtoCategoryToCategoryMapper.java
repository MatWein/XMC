package org.xmc.be.services.category.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.common.factories.BinaryDataFactory;
import org.xmc.be.entities.cashaccount.Category;
import org.xmc.common.stubs.category.DtoCategory;
import org.xmc.common.utils.ImageUtil;

@Component
public class DtoCategoryToCategoryMapper {
    private static final String DESCRIPTION = "category logo";
    private static final int IMAGE_SIZE = 24;

    private final BinaryDataFactory binaryDataFactory;

    @Autowired
    public DtoCategoryToCategoryMapper(BinaryDataFactory binaryDataFactory) {
        this.binaryDataFactory = binaryDataFactory;
    }

    public Category map(DtoCategory dtoCategory) {
        Category category = new Category();
        update(category, dtoCategory);
        return category;
    }

    public void update(Category category, DtoCategory dtoCategory) {
        category.setName(dtoCategory.getName());

        if (dtoCategory.getIcon() != null) {
            byte[] resizedLogo = ImageUtil.resize$(dtoCategory.getIcon(), IMAGE_SIZE, IMAGE_SIZE);
            category.setIcon(binaryDataFactory.create(resizedLogo, DESCRIPTION));
        }
    }
}
