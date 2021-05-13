package io.github.matwein.xmc.fe.stages.main.administration.categories;

import io.github.matwein.xmc.common.services.category.ICategoryService;
import io.github.matwein.xmc.common.stubs.category.CategoryOverviewFields;
import io.github.matwein.xmc.common.stubs.category.DtoCategory;
import io.github.matwein.xmc.common.stubs.category.DtoCategoryOverview;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.stages.main.administration.categories.mapper.CategoryEditDialogMapper;
import io.github.matwein.xmc.fe.ui.CustomDialogBuilder;
import io.github.matwein.xmc.fe.ui.DialogHelper;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.components.table.ExtendedTable;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@FxmlController
public class CategoryController {
    private final ICategoryService categoryService;
    private final AsyncProcessor asyncProcessor;
    private final CategoryEditDialogMapper categoryEditDialogMapper;

    @FXML private ExtendedTable<DtoCategoryOverview, CategoryOverviewFields> tableView;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @Autowired
    public CategoryController(
		    ICategoryService categoryService,
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
        tableView.setDoubleClickConsumer(dtoCategoryOverview -> onEditCategory());
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
        Optional<DtoCategory> dtoCategory = CustomDialogBuilder.getInstance()
                .titleKey(MessageKey.CATEGORY_EDIT_TITLE)
                .addButton(MessageKey.CATEGORY_EDIT_CANCEL, ButtonData.NO)
                .addButton(MessageKey.CATEGORY_EDIT_SAVE, ButtonData.OK_DONE)
                .withFxmlContent(FxmlKey.CATEGORY_EDIT)
                .withMapper(categoryEditDialogMapper)
                .withInput(input)
                .build()
                .showAndWait();

        if (dtoCategory.isPresent()) {
            asyncProcessor.runAsyncVoid(
                    () -> {},
                    monitor -> categoryService.saveOrUpdate(monitor, dtoCategory.get()),
                    () -> tableView.reload()
            );
        }
    }
}
