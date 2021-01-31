package org.xmc.fe.stages.main.depot;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.depot.DepotDeliveryService;
import org.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDelivery;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryOverview;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.depot.mapper.DepotDeliveryEditDialogMapper;
import org.xmc.fe.ui.*;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.table.ExtendedTable;

import java.util.Optional;

@FxmlController
public class DepotDeliveriesController implements IAfterInit<DepotController> {
	private final DepotDeliveryService depotDeliveryService;
	private final AsyncProcessor asyncProcessor;
	private final DepotDeliveryEditDialogMapper depotDeliveryEditDialogMapper;
	
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private Button navigateDepotItemsButton;
	@FXML private ExtendedTable<DtoDepotDeliveryOverview, DepotDeliveryOverviewFields> tableView;
	
	private DepotController parentController;
	
	@Autowired
	public DepotDeliveriesController(
			DepotDeliveryService depotDeliveryService,
			AsyncProcessor asyncProcessor,
			DepotDeliveryEditDialogMapper depotDeliveryEditDialogMapper) {
		
		this.depotDeliveryService = depotDeliveryService;
		this.asyncProcessor = asyncProcessor;
		this.depotDeliveryEditDialogMapper = depotDeliveryEditDialogMapper;
	}
	
	@FXML
	public void initialize() {
		BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
		editButton.disableProperty().bind(noTableItemSelected);
		deleteButton.disableProperty().bind(noTableItemSelected);
		navigateDepotItemsButton.disableProperty().bind(noTableItemSelected);
		
		tableView.setDoubleClickConsumer(dtoDepotTransactionOverview -> onNavigateDepotItems());
	}
	
	@Override
	public void afterInitialize(DepotController parentController) {
		this.parentController = parentController;
		
		long depotId = parentController.getSelectedDepot().getId();
		tableView.setDataProvider((monitor, pagingParams) -> depotDeliveryService.loadOverview(monitor, depotId, pagingParams));
	}
	
	@FXML
	public void onNewDelivery() {
		createOrEditDepotDelivery(null);
	}
	
	@FXML
	public void onEditDelivery() {
		DtoDepotDeliveryOverview selectedDelivery = tableView.getSelectionModel().getSelectedItem();
		createOrEditDepotDelivery(selectedDelivery);
	}
	
	@FXML
	public void onDeleteDelivery() {
		DtoDepotDeliveryOverview selectedDelivery = tableView.getSelectionModel().getSelectedItem();
		String name = MessageAdapter.formatDateTime(selectedDelivery.getDeliveryDate());
		
		if (DialogHelper.showConfirmDialog(MessageKey.DEPOT_DELIVERY_CONFIRM_DELETE, name)) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> depotDeliveryService.markAsDeleted(monitor, selectedDelivery.getId()),
					() -> tableView.reload()
			);
		}
	}
	
	@FXML
	public void onNavigateDepotItems() {
		DtoDepotDeliveryOverview selectedDelivery = tableView.getSelectionModel().getSelectedItem();
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
					() -> tableView.reload()
			);
		}
	}
}
