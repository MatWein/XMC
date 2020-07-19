package org.xmc.fe.stages.main.administration.categories;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.xmc.common.stubs.bank.DtoBank;
import org.xmc.common.stubs.category.CategoryOverviewFields;
import org.xmc.common.stubs.category.DtoCategory;
import org.xmc.fe.ui.CustomDialogBuilder;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.table.TableViewEx;

import java.util.Optional;

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
        createOrEditCategory(null);
    }

    @FXML
    public void onEditCategory() {
        DtoCategory selectedCategory = tableView.getSelectionModel().getSelectedItem();
        createOrEditCategory(selectedCategory);
    }

    @FXML
    public void onDeleteCategory() {
    }

    private void createOrEditCategory(DtoCategory input) {
        Optional<DtoBank> dtoBank = CustomDialogBuilder.getInstance()
                .titleKey(MessageKey.CATEGORY_EDIT_TITLE)
                .addButton(MessageKey.CATEGORY_EDIT_CANCEL, ButtonData.NO)
                .addButton(MessageKey.CATEGORY_EDIT_SAVE, ButtonData.OK_DONE)
                .withFxmlContent(FxmlKey.CATEGORY_EDIT)
//                .withMapper(bankEditDialogMapper)
                .withInput(input)
                .build()
                .showAndWait();

        if (dtoBank.isPresent()) {
//            asyncProcessor.runAsyncVoid(
//                    () -> {},
//                    monitor -> bankService.saveOrUpdate(monitor, dtoBank.get()),
//                    () -> tableView.reload()
//            );
        }
    }
}
