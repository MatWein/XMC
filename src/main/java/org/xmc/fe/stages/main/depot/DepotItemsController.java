package org.xmc.fe.stages.main.depot;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.depot.DepotItemService;
import org.xmc.common.stubs.depot.deliveries.DepotItemOverviewFields;
import org.xmc.common.stubs.depot.deliveries.DtoDepotItemOverview;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;
import org.xmc.fe.ui.components.table.ExtendedTable;

@FxmlController
public class DepotItemsController implements IAfterInit<DepotController> {
	private final DepotItemService depotItemService;
	
	@FXML private ExtendedTable<DtoDepotItemOverview, DepotItemOverviewFields> tableView;
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	
	private DepotController parentController;
	
	@Autowired
	public DepotItemsController(DepotItemService depotItemService) {
		this.depotItemService = depotItemService;
	}
	
	@FXML
	public void initialize() {
		BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
		
		SimpleBooleanProperty multipleTableItemsSelected = new SimpleBooleanProperty(false);
		tableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<DtoDepotItemOverview>)
				change -> multipleTableItemsSelected.set(change.getList().size() > 1));
		
		editButton.disableProperty().bind(noTableItemSelected.or(multipleTableItemsSelected));
		deleteButton.disableProperty().bind(noTableItemSelected);
		
		tableView.setDoubleClickConsumer(dtoCashAccountTransactionOverview -> onEditDepotItem());
	}
	
	@Override
	public void afterInitialize(DepotController parentController) {
		this.parentController = parentController;
		
		long depotDeliveryId = parentController.getSelectedDelivery().getId();
		tableView.setDataProvider((monitor, pagingParams) -> depotItemService.loadOverview(monitor, depotDeliveryId, pagingParams));
	}
	
	@FXML
	public void onNewDepotItem() {
	}
	
	@FXML
	public void onEditDepotItem() {
	}
	
	@FXML
	public void onDeleteDepotItem() {
	}
	
	@FXML
	public void onImportDepotItems() {
	}
}
