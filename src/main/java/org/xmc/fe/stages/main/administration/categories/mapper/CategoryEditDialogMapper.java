package org.xmc.fe.stages.main.administration.categories.mapper;

import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.category.DtoCategory;
import org.xmc.fe.stages.main.administration.categories.CategoryEditController;
import org.xmc.fe.ui.IDialogMapper;

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
        dtoCategory.setName(categoryEditController.getNameTextfield().getText());

        return dtoCategory;
    }
}
