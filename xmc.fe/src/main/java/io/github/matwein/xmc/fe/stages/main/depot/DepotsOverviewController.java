package io.github.matwein.xmc.fe.stages.main.depot;

import io.github.matwein.xmc.common.services.bank.IBankService;
import io.github.matwein.xmc.common.services.depot.IDepotService;
import io.github.matwein.xmc.common.stubs.depot.DepotOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.DtoDepot;
import io.github.matwein.xmc.common.stubs.depot.DtoDepotOverview;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.stages.main.analysis.logic.AnalysisAllAssetsRefreshController;
import io.github.matwein.xmc.fe.stages.main.depot.mapper.DepotEditDialogMapper;
import io.github.matwein.xmc.fe.ui.CustomDialogBuilder;
import io.github.matwein.xmc.fe.ui.DialogHelper;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IAfterInit;
import io.github.matwein.xmc.fe.ui.components.table.ExtendedTable;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@FxmlController
public class DepotsOverviewController implements IAfterInit<DepotController> {
	private final IDepotService depotService;
	private final IBankService bankService;
	private final AsyncProcessor asyncProcessor;
	private final DepotEditDialogMapper depotEditDialogMapper;
	private final AnalysisAllAssetsRefreshController analysisAllAssetsRefreshController;
	
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private Button navigateDeliveriesButton;
	@FXML private Button navigateTransactionsButton;
	@FXML private ExtendedTable<DtoDepotOverview, DepotOverviewFields> tableView;
	
	private DepotController parentController;
	
	@Autowired
	public DepotsOverviewController(
			IDepotService depotService,
			IBankService bankService,
			AsyncProcessor asyncProcessor,
			DepotEditDialogMapper depotEditDialogMapper,
			AnalysisAllAssetsRefreshController analysisAllAssetsRefreshController) {
		
		this.depotService = depotService;
		this.bankService = bankService;
		this.asyncProcessor = asyncProcessor;
		this.depotEditDialogMapper = depotEditDialogMapper;
		this.analysisAllAssetsRefreshController = analysisAllAssetsRefreshController;
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
					this::refreshRelatedViews
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
					this::refreshRelatedViews
			);
		}
	}
	
	private void refreshRelatedViews() {
		tableView.reload();
		analysisAllAssetsRefreshController.refreshAllAssets();
	}
}
