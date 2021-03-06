package io.github.matwein.xmc.fe.stages.main.depot;

import io.github.matwein.xmc.common.services.depot.IDepotTransactionImportService;
import io.github.matwein.xmc.common.services.depot.IDepotTransactionService;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransaction;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransactionOverview;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.stages.main.depot.importing.transactions.populator.DepotTransactionImportStep2Populator;
import io.github.matwein.xmc.fe.stages.main.depot.importing.transactions.populator.DepotTransactionImportStep3Populator;
import io.github.matwein.xmc.fe.stages.main.depot.importing.transactions.populator.DepotTransactionImportStep4Populator;
import io.github.matwein.xmc.fe.stages.main.depot.mapper.DepotTransactionEditDialogMapper;
import io.github.matwein.xmc.fe.ui.CustomDialogBuilder;
import io.github.matwein.xmc.fe.ui.DialogHelper;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IAfterInit;
import io.github.matwein.xmc.fe.ui.components.table.ExtendedTable;
import io.github.matwein.xmc.fe.ui.wizard.WizardBuilder;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.stream.Collectors;

@FxmlController
public class DepotTransactionsController implements IAfterInit<DepotController> {
	private final IDepotTransactionService depotTransactionService;
	private final AsyncProcessor asyncProcessor;
	private final DepotTransactionEditDialogMapper depotTransactionEditDialogMapper;
	private final IDepotTransactionImportService depotTransactionImportService;
	private final DepotTransactionImportStep2Populator depotTransactionImportStep2Populator;
	private final DepotTransactionImportStep3Populator depotTransactionImportStep3Populator;
	private final DepotTransactionImportStep4Populator depotTransactionImportStep4Populator;
	
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private ExtendedTable<DtoDepotTransactionOverview, DepotTransactionOverviewFields> depotTransactionsTableView;
	
	private DepotController parentController;
	
	@Autowired
	public DepotTransactionsController(
			IDepotTransactionService depotTransactionService,
			AsyncProcessor asyncProcessor,
			DepotTransactionEditDialogMapper depotTransactionEditDialogMapper,
			IDepotTransactionImportService depotTransactionImportService,
			DepotTransactionImportStep2Populator depotTransactionImportStep2Populator,
			DepotTransactionImportStep3Populator depotTransactionImportStep3Populator,
			DepotTransactionImportStep4Populator depotTransactionImportStep4Populator) {
		
		this.depotTransactionService = depotTransactionService;
		this.asyncProcessor = asyncProcessor;
		this.depotTransactionEditDialogMapper = depotTransactionEditDialogMapper;
		this.depotTransactionImportService = depotTransactionImportService;
		this.depotTransactionImportStep2Populator = depotTransactionImportStep2Populator;
		this.depotTransactionImportStep3Populator = depotTransactionImportStep3Populator;
		this.depotTransactionImportStep4Populator = depotTransactionImportStep4Populator;
	}
	
	@FXML
	public void initialize() {
		BooleanBinding noTableItemSelected = depotTransactionsTableView.getSelectionModel().selectedItemProperty().isNull();
		
		SimpleBooleanProperty multipleTableItemsSelected = new SimpleBooleanProperty(false);
		depotTransactionsTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<DtoDepotTransactionOverview>)
				change -> multipleTableItemsSelected.set(change.getList().size() > 1));
		
		editButton.disableProperty().bind(noTableItemSelected.or(multipleTableItemsSelected));
		deleteButton.disableProperty().bind(noTableItemSelected);
		
		depotTransactionsTableView.setDoubleClickConsumer(dtoDepotTransactionOverview -> onEditTransaction());
	}
	
	@Override
	public void afterInitialize(DepotController parentController) {
		this.parentController = parentController;
		
		long depotId = parentController.getSelectedDepot().getId();
		depotTransactionsTableView.setDataProvider((monitor, pagingParams) -> depotTransactionService.loadOverview(monitor, depotId, pagingParams));
	}
	
	@FXML
	public void onNewTransaction() {
		createOrEditDepotTransaction(null);
	}
	
	@FXML
	public void onEditTransaction() {
		var selectedDepotTransaction = depotTransactionsTableView.getSelectionModel().getSelectedItem();
		createOrEditDepotTransaction(selectedDepotTransaction);
	}
	
	@FXML
	public void onDeleteTransaction() {
		var selectedDepotTransactionIds = depotTransactionsTableView.getSelectionModel().getSelectedItems()
				.stream()
				.map(DtoDepotTransactionOverview::getId)
				.collect(Collectors.toSet());
		
		if (DialogHelper.showConfirmDialog(MessageKey.DEPOT_TRANSACTION_CONFIRM_DELETE)) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> depotTransactionService.markAsDeleted(monitor, selectedDepotTransactionIds),
					() -> depotTransactionsTableView.reload()
			);
		}
	}
	
	@FXML
	public void onImportTransactions() {
		var importData = new DtoImportData<DepotTransactionImportColmn>();
		long depotId = parentController.getSelectedDepot().getId();
		
		boolean allStepsFinished = WizardBuilder.getInstance()
				.titleKey(MessageKey.DEPOT_TRANSACTION_IMPORT_DIALOG_TITLE)
				.withInput(importData)
				.addStep(MessageKey.DEPOT_TRANSACTION_IMPORT_DIALOG_STEP1_TITLE, FxmlKey.DEPOT_TRANSACTION_IMPORT_DIALOG_STEP1)
				.addStep(MessageKey.DEPOT_TRANSACTION_IMPORT_DIALOG_STEP2_TITLE, FxmlKey.DEPOT_TRANSACTION_IMPORT_DIALOG_STEP2, depotTransactionImportStep2Populator)
				.addStep(MessageKey.DEPOT_TRANSACTION_IMPORT_DIALOG_STEP3_TITLE, FxmlKey.DEPOT_TRANSACTION_IMPORT_DIALOG_STEP3, depotTransactionImportStep3Populator)
				.addStep(MessageKey.DEPOT_TRANSACTION_IMPORT_DIALOG_STEP4_TITLE, FxmlKey.DEPOT_TRANSACTION_IMPORT_DIALOG_STEP4, depotTransactionImportStep4Populator)
				.buildAndShow();
		
		if (allStepsFinished) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> depotTransactionImportService.importTransactions(monitor, depotId, importData),
					() -> depotTransactionsTableView.reload()
			);
		}
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
					() -> depotTransactionsTableView.reload()
			);
		}
	}
}
