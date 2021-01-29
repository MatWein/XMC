package org.xmc.fe.stages.main.depot;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.depot.DepotTransactionService;
import org.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransactionOverview;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;
import org.xmc.fe.ui.components.table.ExtendedTable;

@FxmlController
public class DepotTransactionsController implements IAfterInit<DepotController>  {
	private final DepotTransactionService depotTransactionService;
	
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private ExtendedTable<DtoDepotTransactionOverview, DepotTransactionOverviewFields> tableView;
	
	private DepotController parentController;
	
	@Autowired
	public DepotTransactionsController(DepotTransactionService depotTransactionService) {
		this.depotTransactionService = depotTransactionService;
	}
	
	@FXML
	public void initialize() {
		BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
		
		SimpleBooleanProperty multipleTableItemsSelected = new SimpleBooleanProperty(false);
		tableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<DtoDepotTransactionOverview>)
				change -> multipleTableItemsSelected.set(change.getList().size() > 1));
		
		editButton.disableProperty().bind(noTableItemSelected.or(multipleTableItemsSelected));
		deleteButton.disableProperty().bind(noTableItemSelected);
		
		tableView.setDoubleClickConsumer(dtoDepotTransactionOverview -> onEditTransaction());
	}
	
	@Override
	public void afterInitialize(DepotController parentController) {
		this.parentController = parentController;
		
		long depotId = parentController.getSelectedDepot().getId();
		tableView.setDataProvider((monitor, pagingParams) -> depotTransactionService.loadOverview(monitor, depotId, pagingParams));
	}
	
	@FXML
	public void onNewTransaction() {
	}
	
	@FXML
	public void onEditTransaction() {
	}
	
	@FXML
	public void onDeleteTransaction() {
	}
	
	@FXML
	public void onImportTransactions() {
	}
}
