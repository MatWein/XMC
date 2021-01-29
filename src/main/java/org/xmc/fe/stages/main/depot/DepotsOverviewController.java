package org.xmc.fe.stages.main.depot;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.bank.BankService;
import org.xmc.be.services.depot.DepotService;
import org.xmc.common.stubs.depot.DepotOverviewFields;
import org.xmc.common.stubs.depot.DtoDepot;
import org.xmc.common.stubs.depot.DtoDepotOverview;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.depot.mapper.DepotEditDialogMapper;
import org.xmc.fe.ui.CustomDialogBuilder;
import org.xmc.fe.ui.DialogHelper;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.table.ExtendedTable;

import java.util.Optional;

@FxmlController
public class DepotsOverviewController implements IAfterInit<DepotController> {
	private final DepotService depotService;
	private final BankService bankService;
	private final AsyncProcessor asyncProcessor;
	private final DepotEditDialogMapper depotEditDialogMapper;
	
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private Button navigateDeliveriesButton;
	@FXML private Button navigateTransactionsButton;
	@FXML private ExtendedTable<DtoDepotOverview, DepotOverviewFields> tableView;
	
	private DepotController parentController;
	
	@Autowired
	public DepotsOverviewController(
			DepotService depotService,
			BankService bankService,
			AsyncProcessor asyncProcessor,
			DepotEditDialogMapper depotEditDialogMapper) {
		
		this.depotService = depotService;
		this.bankService = bankService;
		this.asyncProcessor = asyncProcessor;
		this.depotEditDialogMapper = depotEditDialogMapper;
	}
	
	@FXML
	public void initialize() {
		BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
		editButton.disableProperty().bind(noTableItemSelected);
		deleteButton.disableProperty().bind(noTableItemSelected);
		navigateTransactionsButton.disableProperty().bind(noTableItemSelected);
		navigateDeliveriesButton.disableProperty().bind(noTableItemSelected);
		
		tableView.setDataProvider(depotService::loadOverview);
	}
	
	@Override
	public void afterInitialize(DepotController parentController) {
		this.parentController = parentController;
		tableView.setDoubleClickConsumer(parentController::switchToDeliveries);
	}
	
	@FXML
	public void onNewDepot() {
		createOrEditDepot(null);
	}
	
	@FXML
	public void onEditDepot() {
		DtoDepotOverview selectedDepot = tableView.getSelectionModel().getSelectedItem();
		createOrEditDepot(selectedDepot);
	}
	
	@FXML
	public void onDeleteDepot() {
		DtoDepotOverview selectedDepot = tableView.getSelectionModel().getSelectedItem();
		
		if (DialogHelper.showConfirmDialog(MessageKey.DEPOT_CONFIRM_DELETE, selectedDepot.getName())) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> depotService.markAsDeleted(monitor, selectedDepot.getId()),
					() -> tableView.reload()
			);
		}
	}
	
	@FXML
	public void onNavigateDeliveries() {
		DtoDepotOverview selectedDepot = tableView.getSelectionModel().getSelectedItem();
		parentController.switchToDeliveries(selectedDepot);
	}
	
	@FXML
	public void onNavigateTransactions() {
		DtoDepotOverview selectedDepot = tableView.getSelectionModel().getSelectedItem();
		parentController.switchToTransactions(selectedDepot);
	}
	
	private void createOrEditDepot(DtoDepotOverview input) {
		Optional<DtoDepot> dtodtoDepot = CustomDialogBuilder.getInstance()
				.titleKey(MessageKey.DEPOT_EDIT_TITLE)
				.addButton(MessageKey.DEPOT_EDIT_CANCEL, ButtonData.NO)
				.addButton(MessageKey.DEPOT_EDIT_SAVE, ButtonData.OK_DONE)
				.withFxmlContent(FxmlKey.DEPOT_EDIT)
				.withMapper(depotEditDialogMapper)
				.withInput(input)
				.withAsyncDataLoading(bankService::loadAllBanks)
				.build()
				.showAndWait();
		
		if (dtodtoDepot.isPresent()) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> depotService.saveOrUpdate(monitor, dtodtoDepot.get()),
					() -> tableView.reload()
			);
		}
	}
}
