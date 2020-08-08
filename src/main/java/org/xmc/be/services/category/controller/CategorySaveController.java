package org.xmc.be.services.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.Category;
import org.xmc.be.repositories.BinaryDataJpaRepository;
import org.xmc.be.repositories.category.CategoryJpaRepository;
import org.xmc.be.services.category.mapper.DtoCategoryToCategoryMapper;
import org.xmc.common.stubs.category.DtoCategory;

@Component
public class CategorySaveController {
    private final BinaryDataJpaRepository binaryDataJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final DtoCategoryToCategoryMapper dtoCategoryToCategoryMapper;

    @Autowired
    public CategorySaveController(
            BinaryDataJpaRepository binaryDataJpaRepository,
            CategoryJpaRepository categoryJpaRepository,
            DtoCategoryToCategoryMapper dtoCategoryToCategoryMapper) {

        this.binaryDataJpaRepository = binaryDataJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
        this.dtoCategoryToCategoryMapper = dtoCategoryToCategoryMapper;
    }

    public void saveOrUpdate(DtoCategory dtoCategory) {
        Category category = createOrUpdateCategory(dtoCategory);

        if (category.getIcon() != null) {
            binaryDataJpaRepository.save(category.getIcon());
        }
        categoryJpaRepository.save(category);
    }

    private Category createOrUpdateCategory(DtoCategory dtoCategory) {
        if (dtoCategory.getId() == null) {
            return dtoCategoryToCategoryMapper.map(dtoCategory);
        } else {
            Category category = categoryJpaRepository.getOne(dtoCategory.getId());
            dtoCategoryToCategoryMapper.update(category, dtoCategory);
            return category;
        }
    }
}