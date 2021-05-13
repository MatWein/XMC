package io.github.matwein.xmc.fe.stages.main.administration.stocks;

import io.github.matwein.xmc.common.services.category.IStockCategoryService;
import io.github.matwein.xmc.common.services.stock.IStockService;
import io.github.matwein.xmc.common.stubs.stocks.DtoStock;
import io.github.matwein.xmc.common.stubs.stocks.DtoStockOverview;
import io.github.matwein.xmc.common.stubs.stocks.StockOverviewFields;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.stages.main.MainController;
import io.github.matwein.xmc.fe.stages.main.administration.stocks.mapper.StockEditDialogMapper;
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
public class StockController {
	private final IStockCategoryService stockCategoryService;
	private final AsyncProcessor asyncProcessor;
	private final IStockService stockService;
	private final StockEditDialogMapper stockEditDialogMapper;
	
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private ExtendedTable<DtoStockOverview, StockOverviewFields> tableView;
	
	@Autowired
	public StockController(
			IStockCategoryService stockCategoryService,
			AsyncProcessor asyncProcessor,
			IStockService stockService,
			StockEditDialogMapper stockEditDialogMapper) {
		
		this.stockCategoryService = stockCategoryService;
		this.asyncProcessor = asyncProcessor;
		this.stockService = stockService;
		this.stockEditDialogMapper = stockEditDialogMapper;
	}
	
	@FXML
	public void initialize() {
		BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
		editButton.disableProperty().bind(noTableItemSelected);
		deleteButton.disableProperty().bind(noTableItemSelected);
		
		tableView.setDataProvider(stockService::loadOverview);
		tableView.setDoubleClickConsumer(dtoCategoryOverview -> onEditStock());
	}
	
	@FXML
	public void onNewStock() {
		createOrEditStock(null);
	}
	
	@FXML
	public void onEditStock() {
		DtoStock selectedStock = tableView.getSelectionModel().getSelectedItem();
		createOrEditStock(selectedStock);
	}
	
	@FXML
	public void onDeleteStock() {
		DtoStockOverview selectedStock = tableView.getSelectionModel().getSelectedItem();
		
		if (DialogHelper.showConfirmDialog(MessageKey.STOCK_CONFIRM_DELETE, selectedStock.getIsin())) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> stockService.deleteStock(monitor, selectedStock.getId()),
					this::reloadTables
			);
		}
	}
	
	private void createOrEditStock(DtoStock input) {
		Optional<DtoStock> dtoStock = CustomDialogBuilder.getInstance()
				.titleKey(MessageKey.STOCK_EDIT_TITLE)
				.addButton(MessageKey.STOCK_EDIT_CANCEL, ButtonData.NO)
				.addButton(MessageKey.STOCK_EDIT_SAVE, ButtonData.OK_DONE)
				.withFxmlContent(FxmlKey.STOCK_EDIT)
				.withMapper(stockEditDialogMapper)
				.withInput(input)
				.withAsyncDataLoading(stockCategoryService::loadAllStockCategories)
				.build()
				.showAndWait();
		
		if (dtoStock.isPresent()) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> stockService.saveOrUpdate(monitor, dtoStock.get()),
					this::reloadTables
			);
		}
	}
	
	private void reloadTables() {
		tableView.reload();
		
		ExtendedTable<?, ?> depotItemsTableView = (ExtendedTable<?, ?>) MainController.mainWindow.getScene().getRoot().lookup("#depotItemsTableView");
		if (depotItemsTableView != null) {
			depotItemsTableView.reload();
		}
		
		ExtendedTable<?, ?> depotTransactionsTableView = (ExtendedTable<?, ?>) MainController.mainWindow.getScene().getRoot().lookup("#depotTransactionsTableView");
		if (depotTransactionsTableView != null) {
			depotTransactionsTableView.reload();
		}
	}
}
