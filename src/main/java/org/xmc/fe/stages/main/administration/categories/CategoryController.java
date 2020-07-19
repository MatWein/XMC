package org.xmc.fe.stages.main.administration.categories;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.xmc.common.stubs.category.CategoryOverviewFields;
import org.xmc.common.stubs.category.DtoCategory;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.components.table.TableViewEx;

@FxmlController
public class CategoryController {
    @FXML private TableViewEx<DtoCategory, CategoryOverviewFields> tableView;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @FXML
    public void initialize() {
        BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(noTableItemSelected);
        deleteButton.disableProperty().bind(noTableItemSelected);

//        tableView.setDataProvider(bankService::loadOverview);
    }

    @FXML
    public void onNewCategory() {
    }

    @FXML
    public void onEditCategory() {
    }

    @FXML
    public void onDeleteCategory() {
    }
}
