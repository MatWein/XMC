package io.github.matwein.xmc.fe.stages.main.administration.categories.mapper;

import io.github.matwein.xmc.common.stubs.category.DtoStockCategory;
import io.github.matwein.xmc.fe.stages.main.administration.categories.StockCategoryEditController;
import io.github.matwein.xmc.fe.ui.IDialogMapper;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;

@Component
public class StockCategoryEditDialogMapper implements IDialogMapper<StockCategoryEditController, DtoStockCategory> {
    @Override
    public void accept(StockCategoryEditController categoryEditController, DtoStockCategory dtoCategory) {
    	if (dtoCategory == null) {
    		return;
	    }
    	
        categoryEditController.setCategoryId(dtoCategory.getId());

        categoryEditController.getNameTextfield().setText(dtoCategory.getName());
    }

    @Override
    public DtoStockCategory apply(ButtonData buttonData, StockCategoryEditController categoryEditController) {
        if (buttonData != ButtonData.OK_DONE) {
            return null;
        }

        var dtoCategory = new DtoStockCategory();

        dtoCategory.setId(categoryEditController.getCategoryId());
        dtoCategory.setName(categoryEditController.getNameTextfield().getTextOrNull());

        return dtoCategory;
    }
}
