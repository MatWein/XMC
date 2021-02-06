package org.xmc.fe.stages.main.depot;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.depot.DepotDeliveryImportService;
import org.xmc.be.services.depot.DepotDeliveryService;
import org.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import org.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDelivery;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryOverview;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.depot.importing.deliveries.populator.DepotDeliveryImportStep2Populator;
import org.xmc.fe.stages.main.depot.importing.deliveries.populator.DepotDeliveryImportStep3Populator;
import org.xmc.fe.stages.main.depot.importing.deliveries.populator.DepotDeliveryImportStep4Populator;
import org.xmc.fe.stages.main.depot.mapper.DepotDeliveryEditDialogMapper;
import org.xmc.fe.ui.*;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.table.ExtendedTable;
import org.xmc.fe.ui.wizard.WizardBuilder;

import java.util.Optional;

@FxmlController
public class DepotDeliveriesController implements IAfterInit<DepotController> {
	private final DepotDeliveryService depotDeliveryService;
	private final AsyncProcessor asyncProcessor;
	private final DepotDeliveryEditDialogMapper depotDeliveryEditDialogMapper;
	private final DepotDeliveryImportService depotDeliveryImportService;
	private final DepotDeliveryImportStep2Populator depotDeliveryImportStep2Populator;
	private final DepotDeliveryImportStep3Populator depotDeliveryImportStep3Populator;
	private final DepotDeliveryImportStep4Populator depotDeliveryImportStep4Populator;
	
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private Button navigateDepotItemsButton;
	@FXML private ExtendedTable<DtoDepotDeliveryOverview, DepotDeliveryOverviewFields> depotDeliveriesTableView;
	
	private DepotController parentController;
	
	@Autowired
	public DepotDeliveriesController(
			DepotDeliveryService depotDeliveryService,
			AsyncProcessor asyncProcessor,
			DepotDeliveryEditDialogMapper depotDeliveryEditDialogMapper,
			DepotDeliveryImportService depotDeliveryImportService,
			DepotDeliveryImportStep2Populator depotDeliveryImportStep2Populator,
			DepotDeliveryImportStep3Populator depotDeliveryImportStep3Populator,
			DepotDeliveryImportStep4Populator depotDeliveryImportStep4Populator) {
		
		this.depotDeliveryService = depotDeliveryService;
		this.asyncProcessor = asyncProcessor;
		this.depotDeliveryEditDialogMapper = depotDeliveryEditDialogMapper;
		this.depotDeliveryImportService = depotDeliveryImportService;
		this.depotDeliveryImportStep2Populator = depotDeliveryImportStep2Populator;
		this.depotDeliveryImportStep3Populator = depotDeliveryImportStep3Populator;
		this.depotDeliveryImportStep4Populator = depotDeliveryImportStep4Populator;
	}
	
	@FXML
	public void initialize() {
		BooleanBinding noTableItemSelected = depotDeliveriesTableView.getSelectionModel().selectedItemProperty().isNull();
		editButton.disableProperty().bind(noTableItemSelected);
		deleteButton.disableProperty().bind(noTableItemSelected);
		navigateDepotItemsButton.disableProperty().bind(noTableItemSelected);
		
		depotDeliveriesTableView.setDoubleClickConsumer(dtoDepotTransactionOverview -> onNavigateDepotItems());
	}
	
	@Override
	public void afterInitialize(DepotController parentController) {
		this.parentController = parentController;
		
		long depotId = parentController.getSelectedDepot().getId();
		depotDeliveriesTableView.setDataProvider((monitor, pagingParams) -> depotDeliveryService.loadOverview(monitor, depotId, pagingParams));
	}
	
	@FXML
	public void onNewDelivery() {
		createOrEditDepotDelivery(null);
	}
	
	@FXML
	public void onEditDelivery() {
		DtoDepotDeliveryOverview selectedDelivery = depotDeliveriesTableView.getSelectionModel().getSelectedItem();
		createOrEditDepotDelivery(selectedDelivery);
	}
	
	@FXML
	public void onDeleteDelivery() {
		DtoDepotDeliveryOverview selectedDelivery = depotDeliveriesTableView.getSelectionModel().getSelectedItem();
		String name = MessageAdapter.formatDateTime(selectedDelivery.getDeliveryDate());
		
		if (DialogHelper.showConfirmDialog(MessageKey.DEPOT_DELIVERY_CONFIRM_DELETE, name)) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> depotDeliveryService.markAsDeleted(monitor, selectedDelivery.getId()),
					() -> depotDeliveriesTableView.reload()
			);
		}
	}
	
	@FXML
	public void onNavigateDepotItems() {
		DtoDepotDeliveryOverview selectedDelivery = depotDeliveriesTableView.getSelectionModel().getSelectedItem();
		parentController.switchToDepotItems(selectedDelivery);
	}
	
	private void createOrEditDepotDelivery(DtoDepotDeliveryOverview input) {
		long depotId = parentController.getSelectedDepot().getId();
		
		Optional<DtoDepotDelivery> dtoDepotDelivery = CustomDialogBuilder.getInstance()
				.titleKey(MessageKey.DEPOT_DELIVERY_EDIT_TITLE)
				.addButton(MessageKey.DEPOT_DELIVERY_EDIT_CANCEL, ButtonData.NO)
				.addButton(MessageKey.DEPOT_DELIVERY_EDIT_SAVE, ButtonData.OK_DONE)
				.withFxmlContent(FxmlKey.DEPOT_DELIVERY_EDIT)
				.withMapper(depotDeliveryEditDialogMapper)
				.withInput(input)
				.build()
				.showAndWait();
		
		if (dtoDepotDelivery.isPresent()) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> depotDeliveryService.saveOrUpdate(monitor, depotId, dtoDepotDelivery.get()),
					() -> depotDeliveriesTableView.reload()
			);
		}
	}
	
	@FXML
	public void onImportDeliveries() {
		var importData = new DtoImportData<DepotDeliveryImportColmn>();
		long depotId = parentController.getSelectedDepot().getId();
		
		boolean allStepsFinished = WizardBuilder.getInstance()
				.titleKey(MessageKey.DEPOT_DELIVERY_IMPORT_DIALOG_TITLE)
				.withInput(importData)
				.addStep(MessageKey.DEPOT_DELIVERY_IMPORT_DIALOG_STEP1_TITLE, FxmlKey.DEPOT_DELIVERY_IMPORT_DIALOG_STEP1)
				.addStep(MessageKey.DEPOT_DELIVERY_IMPORT_DIALOG_STEP2_TITLE, FxmlKey.DEPOT_DELIVERY_IMPORT_DIALOG_STEP2, depotDeliveryImportStep2Populator)
				.addStep(MessageKey.DEPOT_DELIVERY_IMPORT_DIALOG_STEP3_TITLE, FxmlKey.DEPOT_DELIVERY_IMPORT_DIALOG_STEP3, depotDeliveryImportStep3Populator)
				.addStep(MessageKey.DEPOT_DELIVERY_IMPORT_DIALOG_STEP4_TITLE, FxmlKey.DEPOT_DELIVERY_IMPORT_DIALOG_STEP4, depotDeliveryImportStep4Populator)
				.buildAndShow();
		
		if (allStepsFinished) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> depotDeliveryImportService.importTransactions(monitor, depotId, importData),
					() -> depotDeliveriesTableView.reload()
			);
		}
	}
}
