package io.github.matwein.xmc.fe.stages.main.depot;

import io.github.matwein.xmc.common.services.depot.IDepotItemImportService;
import io.github.matwein.xmc.common.services.depot.IDepotItemService;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemImportColmn;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemOverviewFields;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItem;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemOverview;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.stages.main.depot.importing.items.populator.DepotItemImportStep2Populator;
import io.github.matwein.xmc.fe.stages.main.depot.importing.items.populator.DepotItemImportStep3Populator;
import io.github.matwein.xmc.fe.stages.main.depot.importing.items.populator.DepotItemImportStep4Populator;
import io.github.matwein.xmc.fe.stages.main.depot.mapper.DepotItemEditDialogMapper;
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
public class DepotItemsController implements IAfterInit<DepotController> {
	private final IDepotItemService depotItemService;
	private final AsyncProcessor asyncProcessor;
	private final DepotItemEditDialogMapper depotItemEditDialogMapper;
	private final IDepotItemImportService depotItemImportService;
	private final DepotItemImportStep2Populator depotItemImportStep2Populator;
	private final DepotItemImportStep3Populator depotItemImportStep3Populator;
	private final DepotItemImportStep4Populator depotItemImportStep4Populator;
	
	@FXML private ExtendedTable<DtoDepotItemOverview, DepotItemOverviewFields> depotItemsTableView;
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	
	private DepotController parentController;
	
	@Autowired
	public DepotItemsController(
			IDepotItemService depotItemService,
			AsyncProcessor asyncProcessor,
			DepotItemEditDialogMapper depotItemEditDialogMapper,
			IDepotItemImportService depotItemImportService,
			DepotItemImportStep2Populator depotItemImportStep2Populator,
			DepotItemImportStep3Populator depotItemImportStep3Populator,
			DepotItemImportStep4Populator depotItemImportStep4Populator) {
		
		this.depotItemService = depotItemService;
		this.asyncProcessor = asyncProcessor;
		this.depotItemEditDialogMapper = depotItemEditDialogMapper;
		this.depotItemImportService = depotItemImportService;
		this.depotItemImportStep2Populator = depotItemImportStep2Populator;
		this.depotItemImportStep3Populator = depotItemImportStep3Populator;
		this.depotItemImportStep4Populator = depotItemImportStep4Populator;
	}
	
	@FXML
	public void initialize() {
		BooleanBinding noTableItemSelected = depotItemsTableView.getSelectionModel().selectedItemProperty().isNull();
		
		SimpleBooleanProperty multipleTableItemsSelected = new SimpleBooleanProperty(false);
		depotItemsTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<DtoDepotItemOverview>)
				change -> multipleTableItemsSelected.set(change.getList().size() > 1));
		
		editButton.disableProperty().bind(noTableItemSelected.or(multipleTableItemsSelected));
		deleteButton.disableProperty().bind(noTableItemSelected);
		
		depotItemsTableView.setDoubleClickConsumer(dtoCashAccountTransactionOverview -> onEditDepotItem());
	}
	
	@Override
	public void afterInitialize(DepotController parentController) {
		this.parentController = parentController;
		
		long depotDeliveryId = parentController.getSelectedDelivery().getId();
		depotItemsTableView.setDataProvider((monitor, pagingParams) -> depotItemService.loadOverview(monitor, depotDeliveryId, pagingParams));
	}
	
	@FXML
	public void onNewDepotItem() {
		createOrEditDepotItem(null);
	}
	
	@FXML
	public void onEditDepotItem() {
		var selectedDepotItem = depotItemsTableView.getSelectionModel().getSelectedItem();
		createOrEditDepotItem(selectedDepotItem);
	}
	
	@FXML
	public void onDeleteDepotItem() {
		var selectedDepotItemIds = depotItemsTableView.getSelectionModel().getSelectedItems()
				.stream()
				.map(DtoDepotItemOverview::getId)
				.collect(Collectors.toSet());
		
		long depotDeliveryId = parentController.getSelectedDelivery().getId();
		
		if (DialogHelper.showConfirmDialog(MessageKey.DEPOT_ITEM_CONFIRM_DELETE)) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> depotItemService.markAsDeleted(monitor, depotDeliveryId, selectedDepotItemIds),
					() -> depotItemsTableView.reload()
			);
		}
	}
	
	@FXML
	public void onImportDepotItems() {
		var importData = new DtoImportData<DepotItemImportColmn>();
		long depotDeliveryId = parentController.getSelectedDelivery().getId();
		
		boolean allStepsFinished = WizardBuilder.getInstance()
				.titleKey(MessageKey.DEPOT_ITEM_IMPORT_DIALOG_TITLE)
				.withInput(importData)
				.addStep(MessageKey.DEPOT_ITEM_IMPORT_DIALOG_STEP1_TITLE, FxmlKey.DEPOT_ITEM_IMPORT_DIALOG_STEP1)
				.addStep(MessageKey.DEPOT_ITEM_IMPORT_DIALOG_STEP2_TITLE, FxmlKey.DEPOT_ITEM_IMPORT_DIALOG_STEP2, depotItemImportStep2Populator)
				.addStep(MessageKey.DEPOT_ITEM_IMPORT_DIALOG_STEP3_TITLE, FxmlKey.DEPOT_ITEM_IMPORT_DIALOG_STEP3, depotItemImportStep3Populator)
				.addStep(MessageKey.DEPOT_ITEM_IMPORT_DIALOG_STEP4_TITLE, FxmlKey.DEPOT_ITEM_IMPORT_DIALOG_STEP4, depotItemImportStep4Populator)
				.buildAndShow();
		
		if (allStepsFinished) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> depotItemImportService.importDeliveries(monitor, depotDeliveryId, importData),
					() -> depotItemsTableView.reload()
			);
		}
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
					() -> depotItemsTableView.reload()
			);
		}
	}
}
