package io.github.matwein.xmc.fe.stages.main.administration.categories;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.matwein.xmc.be.services.category.StockCategoryService;
import io.github.matwein.xmc.common.stubs.category.DtoStockCategory;
import io.github.matwein.xmc.common.stubs.category.DtoStockCategoryOverview;
import io.github.matwein.xmc.common.stubs.category.StockCategoryOverviewFields;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.stages.main.administration.categories.mapper.StockCategoryEditDialogMapper;
import io.github.matwein.xmc.fe.ui.CustomDialogBuilder;
import io.github.matwein.xmc.fe.ui.DialogHelper;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.components.table.ExtendedTable;

import java.util.Optional;

@FxmlController
public class StockCategoryController {
    private final AsyncProcessor asyncProcessor;
	private final StockCategoryService stockCategoryService;
	private final StockCategoryEditDialogMapper stockCategoryEditDialogMapper;
	
	@FXML private ExtendedTable<DtoStockCategoryOverview, StockCategoryOverviewFields> tableView;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @Autowired
    public StockCategoryController(
		    AsyncProcessor asyncProcessor,
		    StockCategoryService stockCategoryService,
		    StockCategoryEditDialogMapper stockCategoryEditDialogMapper) {

        this.asyncProcessor = asyncProcessor;
	    this.stockCategoryService = stockCategoryService;
	    this.stockCategoryEditDialogMapper = stockCategoryEditDialogMapper;
    }

    @FXML
    public void initialize() {
        BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(noTableItemSelected);
        deleteButton.disableProperty().bind(noTableItemSelected);

        tableView.setDataProvider(stockCategoryService::loadOverview);
        tableView.setDoubleClickConsumer(dtoCategoryOverview -> onEditCategory());
    }

    @FXML
    public void onNewCategory() {
        createOrEditCategory(null);
    }

    @FXML
    public void onEditCategory() {
        DtoStockCategory selectedCategory = tableView.getSelectionModel().getSelectedItem();
        createOrEditCategory(selectedCategory);
    }

    @FXML
    public void onDeleteCategory() {
        DtoStockCategoryOverview selectedCategory = tableView.getSelectionModel().getSelectedItem();

        if (DialogHelper.showConfirmDialog(MessageKey.CATEGORY_CONFIRM_DELETE, selectedCategory.getName())) {
            asyncProcessor.runAsyncVoid(
                    () -> {},
                    monitor -> stockCategoryService.markAsDeleted(monitor, selectedCategory.getId()),
                    () -> tableView.reload()
            );
        }
    }

    private void createOrEditCategory(DtoStockCategory input) {
        Optional<DtoStockCategory> dtoCategory = CustomDialogBuilder.getInstance()
                .titleKey(MessageKey.STOCK_CATEGORY_EDIT_TITLE)
                .addButton(MessageKey.STOCK_CATEGORY_EDIT_CANCEL, ButtonData.NO)
                .addButton(MessageKey.STOCK_CATEGORY_EDIT_SAVE, ButtonData.OK_DONE)
                .withFxmlContent(FxmlKey.STOCK_CATEGORY_EDIT)
                .withMapper(stockCategoryEditDialogMapper)
                .withInput(input)
                .build()
                .showAndWait();

        if (dtoCategory.isPresent()) {
            asyncProcessor.runAsyncVoid(
                    () -> {},
                    monitor -> stockCategoryService.saveOrUpdate(monitor, dtoCategory.get()),
                    () -> tableView.reload()
            );
        }
    }
}
