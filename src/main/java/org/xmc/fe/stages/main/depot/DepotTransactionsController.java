package org.xmc.fe.stages.main.depot;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.depot.DepotTransactionService;
import org.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransaction;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransactionOverview;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.depot.mapper.DepotTransactionEditDialogMapper;
import org.xmc.fe.ui.CustomDialogBuilder;
import org.xmc.fe.ui.DialogHelper;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.table.ExtendedTable;

import java.util.Optional;
import java.util.stream.Collectors;

@FxmlController
public class DepotTransactionsController implements IAfterInit<DepotController>  {
	private final DepotTransactionService depotTransactionService;
	private final AsyncProcessor asyncProcessor;
	private final DepotTransactionEditDialogMapper depotTransactionEditDialogMapper;
	
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private ExtendedTable<DtoDepotTransactionOverview, DepotTransactionOverviewFields> tableView;
	
	private DepotController parentController;
	
	@Autowired
	public DepotTransactionsController(
			DepotTransactionService depotTransactionService,
			AsyncProcessor asyncProcessor,
			DepotTransactionEditDialogMapper depotTransactionEditDialogMapper) {
		
		this.depotTransactionService = depotTransactionService;
		this.asyncProcessor = asyncProcessor;
		this.depotTransactionEditDialogMapper = depotTransactionEditDialogMapper;
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
		createOrEditDepotTransaction(null);
	}
	
	@FXML
	public void onEditTransaction() {
		var selectedDepotTransaction = tableView.getSelectionModel().getSelectedItem();
		createOrEditDepotTransaction(selectedDepotTransaction);
	}
	
	@FXML
	public void onDeleteTransaction() {
		var selectedDepotTransactionIds = tableView.getSelectionModel().getSelectedItems()
				.stream()
				.map(DtoDepotTransactionOverview::getId)
				.collect(Collectors.toSet());
		
		if (DialogHelper.showConfirmDialog(MessageKey.DEPOT_TRANSACTION_CONFIRM_DELETE)) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> depotTransactionService.markAsDeleted(monitor, selectedDepotTransactionIds),
					() -> tableView.reload()
			);
		}
	}
	
	@FXML
	public void onImportTransactions() {
	}
	
	private void createOrEditDepotTransaction(DtoDepotTransactionOverview input) {
		long depotId = parentController.getSelectedDepot().getId();
		
		Optional<DtoDepotTransaction> dtoDepotTransaction = CustomDialogBuilder.getInstance()
				.titleKey(MessageKey.DEPOT_TRANSACTION_EDIT_TITLE)
				.addButton(MessageKey.DEPOT_TRANSACTION_EDIT_CANCEL, ButtonData.NO)
				.addButton(MessageKey.DEPOT_TRANSACTION_EDIT_SAVE, ButtonData.OK_DONE)
				.withFxmlContent(FxmlKey.DEPOT_TRANSACTION_EDIT)
				.withMapper(depotTransactionEditDialogMapper)
				.withInput(input)
				.build()
				.showAndWait();
		
		if (dtoDepotTransaction.isPresent()) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> depotTransactionService.saveOrUpdate(monitor, depotId, dtoDepotTransaction.get()),
					() -> tableView.reload()
			);
		}
	}
}
