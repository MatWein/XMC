package io.github.matwein.xmc.fe.stages.main.administration.categories.mapper;

import io.github.matwein.xmc.common.stubs.category.DtoCategory;
import io.github.matwein.xmc.fe.stages.main.administration.categories.CategoryEditController;
import io.github.matwein.xmc.fe.ui.IDialogMapper;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;

@Component
public class CategoryEditDialogMapper implements IDialogMapper<CategoryEditController, DtoCategory> {
    @Override
    public void accept(CategoryEditController categoryEditController, DtoCategory dtoCategory) {
        categoryEditController.setCategoryId(dtoCategory.getId());

        categoryEditController.getIconButton().setImage(dtoCategory.getIcon());
        categoryEditController.getNameTextfield().setText(dtoCategory.getName());
    }

    @Override
    public DtoCategory apply(ButtonData buttonData, CategoryEditController categoryEditController) {
        if (buttonData != ButtonData.OK_DONE) {
            return null;
        }

        var dtoCategory = new DtoCategory();

        dtoCategory.setIcon(categoryEditController.getIconButton().getImageAsByteArray());
        dtoCategory.setId(categoryEditController.getCategoryId());
        dtoCategory.setName(categoryEditController.getNameTextfield().getTextOrNull());

        return dtoCategory;
    }
}
