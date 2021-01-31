package org.xmc.fe.stages.main.depot;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.depot.DepotItemService;
import org.xmc.common.stubs.depot.deliveries.DepotItemOverviewFields;
import org.xmc.common.stubs.depot.deliveries.DtoDepotItem;
import org.xmc.common.stubs.depot.deliveries.DtoDepotItemOverview;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.depot.mapper.DepotItemEditDialogMapper;
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
public class DepotItemsController implements IAfterInit<DepotController> {
	private final DepotItemService depotItemService;
	private final AsyncProcessor asyncProcessor;
	private final DepotItemEditDialogMapper depotItemEditDialogMapper;
	
	@FXML private ExtendedTable<DtoDepotItemOverview, DepotItemOverviewFields> tableView;
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	
	private DepotController parentController;
	
	@Autowired
	public DepotItemsController(
			DepotItemService depotItemService,
			AsyncProcessor asyncProcessor,
			DepotItemEditDialogMapper depotItemEditDialogMapper) {
		
		this.depotItemService = depotItemService;
		this.asyncProcessor = asyncProcessor;
		this.depotItemEditDialogMapper = depotItemEditDialogMapper;
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
		createOrEditDepotItem(null);
	}
	
	@FXML
	public void onEditDepotItem() {
		var selectedDepotItem = tableView.getSelectionModel().getSelectedItem();
		createOrEditDepotItem(selectedDepotItem);
	}
	
	@FXML
	public void onDeleteDepotItem() {
		var selectedDepotItemIds = tableView.getSelectionModel().getSelectedItems()
				.stream()
				.map(DtoDepotItemOverview::getId)
				.collect(Collectors.toSet());
		
		if (DialogHelper.showConfirmDialog(MessageKey.DEPOT_ITEM_CONFIRM_DELETE)) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> depotItemService.markAsDeleted(monitor, selectedDepotItemIds),
					() -> tableView.reload()
			);
		}
	}
	
	@FXML
	public void onImportDepotItems() {
	}
	
	private void createOrEditDepotItem(DtoDepotItemOverview input) {
		long depotDeliveryId = parentController.getSelectedDelivery().getId();
		
		Optional<DtoDepotItem> dtoDepotItem = CustomDialogBuilder.getInstance()
				.titleKey(MessageKey.DEPOT_ITEM_EDIT_TITLE)
				.addButton(MessageKey.DEPOT_ITEM_EDIT_CANCEL, ButtonData.NO)
				.addButton(MessageKey.DEPOT_ITEM_EDIT_SAVE, ButtonData.OK_DONE)
				.withFxmlContent(FxmlKey.DEPOT_ITEM_EDIT)
				.withMapper(depotItemEditDialogMapper)
				.withInput(input)
				.build()
				.showAndWait();
		
		if (dtoDepotItem.isPresent()) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> depotItemService.saveOrUpdate(monitor, depotDeliveryId, dtoDepotItem.get()),
					() -> tableView.reload()
			);
		}
	}
}
