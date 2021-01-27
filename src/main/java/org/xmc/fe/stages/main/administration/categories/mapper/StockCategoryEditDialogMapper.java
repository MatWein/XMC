package org.xmc.fe.stages.main.administration.categories.mapper;

import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.category.DtoStockCategory;
import org.xmc.fe.stages.main.administration.categories.StockCategoryEditController;
import org.xmc.fe.ui.IDialogMapper;

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
        dtoCategory.setName(categoryEditController.getNameTextfield().getText());

        return dtoCategory;
    }
}
