package org.xmc.fe.stages.main.administration.stocks;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.category.StockCategoryService;
import org.xmc.be.services.stock.StockService;
import org.xmc.common.stubs.stocks.DtoStock;
import org.xmc.common.stubs.stocks.DtoStockOverview;
import org.xmc.common.stubs.stocks.StockOverviewFields;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.ui.CustomDialogBuilder;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.table.ExtendedTable;

import java.util.Optional;

@FxmlController
public class StockController {
	private final StockCategoryService stockCategoryService;
	private final AsyncProcessor asyncProcessor;
	private final StockService stockService;
	
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private ExtendedTable<DtoStockOverview, StockOverviewFields> tableView;
	
	@Autowired
	public StockController(
			StockCategoryService stockCategoryService,
			AsyncProcessor asyncProcessor,
			StockService stockService) {
		
		this.stockCategoryService = stockCategoryService;
		this.asyncProcessor = asyncProcessor;
		this.stockService = stockService;
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
	
	}
	
	private void createOrEditStock(DtoStock input) {
		Optional<DtoStock> dtoStock = CustomDialogBuilder.getInstance()
				.titleKey(MessageKey.CASHACCOUNT_TRANSACTION_EDIT_TITLE)
				.addButton(MessageKey.CASHACCOUNT_TRANSACTION_EDIT_CANCEL, ButtonData.NO)
				.addButton(MessageKey.CASHACCOUNT_TRANSACTION_EDIT_SAVE, ButtonData.OK_DONE)
				.withFxmlContent(FxmlComponentFactory.FxmlKey.CASH_ACCOUNT_TRANSACTION_EDIT)
//				.withMapper(cashAccountTransactionEditDialogMapper)
				.withInput(input)
				.withAsyncDataLoading(stockCategoryService::loadAllStockCategories)
				.build()
				.showAndWait();
		
		if (dtoStock.isPresent()) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> stockService.saveOrUpdate(monitor, dtoStock.get()),
					() -> tableView.reload()
			);
		}
	}
}
