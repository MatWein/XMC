package io.github.matwein.xmc.be.services.category.mapper;

import io.github.matwein.xmc.be.common.ImageUtil;
import io.github.matwein.xmc.be.common.factories.BinaryDataFactory;
import io.github.matwein.xmc.be.entities.cashaccount.Category;
import io.github.matwein.xmc.common.stubs.category.DtoCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DtoCategoryToCategoryMapper {
    private static final String DESCRIPTION = "category logo";
    private static final int IMAGE_SIZE = 24;

    private final BinaryDataFactory binaryDataFactory;
	private final ImageUtil imageUtil;
	
	@Autowired
    public DtoCategoryToCategoryMapper(
    		BinaryDataFactory binaryDataFactory,
		    ImageUtil imageUtil) {
		
        this.binaryDataFactory = binaryDataFactory;
		this.imageUtil = imageUtil;
	}

    public Category map(DtoCategory dtoCategory) {
        Category category = new Category();
        update(category, dtoCategory);
        return category;
    }

    public void update(Category category, DtoCategory dtoCategory) {
        category.setName(dtoCategory.getName());

        if (dtoCategory.getIcon() != null) {
            byte[] resizedLogo = imageUtil.resize$(dtoCategory.getIcon(), IMAGE_SIZE, IMAGE_SIZE);
            category.setIcon(binaryDataFactory.create(resizedLogo, DESCRIPTION));
        }
    }
}
