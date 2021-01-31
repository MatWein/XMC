package org.xmc.fe.stages.main.depot;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.depot.DepotDeliveryService;
import org.xmc.common.stubs.depot.deliveries.DepotDeliveryOverviewFields;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryOverview;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;
import org.xmc.fe.ui.components.table.ExtendedTable;

@FxmlController
public class DepotDeliveriesController implements IAfterInit<DepotController> {
	private final DepotDeliveryService depotDeliveryService;
	
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private Button navigateDepotItemsButton;
	@FXML private ExtendedTable<DtoDepotDeliveryOverview, DepotDeliveryOverviewFields> tableView;
	
	private DepotController parentController;
	
	@Autowired
	public DepotDeliveriesController(DepotDeliveryService depotDeliveryService) {
		this.depotDeliveryService = depotDeliveryService;
	}
	
	@FXML
	public void initialize() {
		BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
		editButton.disableProperty().bind(noTableItemSelected);
		deleteButton.disableProperty().bind(noTableItemSelected);
		navigateDepotItemsButton.disableProperty().bind(noTableItemSelected);
		
		tableView.setDoubleClickConsumer(dtoDepotTransactionOverview -> onEditDelivery());
	}
	
	@Override
	public void afterInitialize(DepotController parentController) {
		this.parentController = parentController;
		
		long depotId = parentController.getSelectedDepot().getId();
		tableView.setDataProvider((monitor, pagingParams) -> depotDeliveryService.loadOverview(monitor, depotId, pagingParams));
	}
	
	@FXML
	public void onNewDelivery() {
	}
	
	@FXML
	public void onEditDelivery() {
	}
	
	@FXML
	public void onDeleteDelivery() {
	}
	
	@FXML
	public void onNavigateDepotItems() {
		DtoDepotDeliveryOverview selectedDelivery = tableView.getSelectionModel().getSelectedItem();
		parentController.switchToDepotItems(selectedDelivery);
	}
}
