package org.xmc.fe.stages.main.administration.categories;

import javafx.fxml.FXML;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.validation.components.ValidationTextField;

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
