package io.github.matwein.xmc.fe.stages.main.administration.categories;

import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationTextField;
import javafx.fxml.FXML;

@FxmlController
public class StockCategoryEditController {
    private Long categoryId;

    @FXML private ValidationTextField nameTextfield;

    public ValidationTextField getNameTextfield() {
        return nameTextfield;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
