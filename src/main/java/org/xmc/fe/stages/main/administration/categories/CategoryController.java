package org.xmc.fe.stages.main.administration.categories;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.category.CategoryService;
import org.xmc.common.stubs.category.CategoryOverviewFields;
import org.xmc.common.stubs.category.DtoCategory;
import org.xmc.common.stubs.category.DtoCategoryOverview;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.administration.categories.mapper.CategoryEditDialogMapper;
import org.xmc.fe.ui.CustomDialogBuilder;
import org.xmc.fe.ui.DialogHelper;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.table.TableViewEx;

import java.util.Optional;

@FxmlController
public class CategoryController {
    private final CategoryService categoryService;
    private final AsyncProcessor asyncProcessor;
    private final CategoryEditDialogMapper categoryEditDialogMapper;

    @FXML private TableViewEx<DtoCategoryOverview, CategoryOverviewFields> tableView;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @Autowired
    public CategoryController(
            CategoryService categoryService,
            AsyncProcessor asyncProcessor,
            CategoryEditDialogMapper categoryEditDialogMapper) {

        this.categoryService = categoryService;
        this.asyncProcessor = asyncProcessor;
        this.categoryEditDialogMapper = categoryEditDialogMapper;
    }

    @FXML
    public void initialize() {
        BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(noTableItemSelected);
        deleteButton.disableProperty().bind(noTableItemSelected);

        tableView.setDataProvider(categoryService::loadOverview);
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
        DtoCategoryOverview selectedCategory = tableView.getSelectionModel().getSelectedItem();

        if (DialogHelper.showConfirmDialog(MessageKey.CATEGORY_CONFIRM_DELETE, selectedCategory.getName())) {
            asyncProcessor.runAsyncVoid(
                    () -> {},
                    monitor -> categoryService.markAsDeleted(monitor, selectedCategory.getId()),
                    () -> tableView.reload()
            );
        }
    }

    private void createOrEditCategory(DtoCategory input) {
        Optional<DtoCategory> dtodtoCategory = CustomDialogBuilder.getInstance()
                .titleKey(MessageKey.CATEGORY_EDIT_TITLE)
                .addButton(MessageKey.CATEGORY_EDIT_CANCEL, ButtonData.NO)
                .addButton(MessageKey.CATEGORY_EDIT_SAVE, ButtonData.OK_DONE)
                .withFxmlContent(FxmlKey.CATEGORY_EDIT)
                .withMapper(categoryEditDialogMapper)
                .withInput(input)
                .build()
                .showAndWait();

        if (dtodtoCategory.isPresent()) {
            asyncProcessor.runAsyncVoid(
                    () -> {},
                    monitor -> categoryService.saveOrUpdate(monitor, dtodtoCategory.get()),
                    () -> tableView.reload()
            );
        }
    }
}
