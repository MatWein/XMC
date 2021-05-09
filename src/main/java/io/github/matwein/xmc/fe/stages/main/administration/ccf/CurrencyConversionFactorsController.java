package io.github.matwein.xmc.fe.stages.main.administration.ccf;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.matwein.xmc.be.services.ccf.CurrencyConversionFactorService;
import io.github.matwein.xmc.common.stubs.ccf.CurrencyConversionFactorOverviewFields;
import io.github.matwein.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.stages.main.MainController;
import io.github.matwein.xmc.fe.stages.main.administration.ccf.mapper.CurrencyConversionFactorEditDialogMapper;
import io.github.matwein.xmc.fe.ui.CustomDialogBuilder;
import io.github.matwein.xmc.fe.ui.DialogHelper;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.components.table.ExtendedTable;

import java.util.Optional;

@FxmlController
public class CurrencyConversionFactorsController {
	private final CurrencyConversionFactorService currencyConversionFactorService;
	private final AsyncProcessor asyncProcessor;
	private final CurrencyConversionFactorEditDialogMapper currencyConversionFactorEditDialogMapper;
	
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private ExtendedTable<DtoCurrencyConversionFactor, CurrencyConversionFactorOverviewFields> tableView;
	
	@Autowired
	public CurrencyConversionFactorsController(
			CurrencyConversionFactorService currencyConversionFactorService,
			AsyncProcessor asyncProcessor,
			CurrencyConversionFactorEditDialogMapper currencyConversionFactorEditDialogMapper) {
		
		this.currencyConversionFactorService = currencyConversionFactorService;
		this.asyncProcessor = asyncProcessor;
		this.currencyConversionFactorEditDialogMapper = currencyConversionFactorEditDialogMapper;
	}
	
	@FXML
	public void initialize() {
		BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
		editButton.disableProperty().bind(noTableItemSelected);
		deleteButton.disableProperty().bind(noTableItemSelected);
		
		tableView.setDataProvider(currencyConversionFactorService::loadOverview);
		tableView.setDoubleClickConsumer(dtoCategoryOverview -> onEditCurrencyConversionFactor());
	}
	
	@FXML
	public void onNewCurrencyConversionFactor() {
		createOrEditCurrencyConversionFactor(null);
	}
	
	@FXML
	public void onEditCurrencyConversionFactor() {
		DtoCurrencyConversionFactor selectedCurrencyConversionFactor = tableView.getSelectionModel().getSelectedItem();
		createOrEditCurrencyConversionFactor(selectedCurrencyConversionFactor);
	}
	
	@FXML
	public void onDeleteCurrencyConversionFactor() {
		DtoCurrencyConversionFactor selectedCurrencyConversionFactor = tableView.getSelectionModel().getSelectedItem();
		
		if (DialogHelper.showConfirmDialog(MessageKey.CURRENCY_CONVERSION_FACTOR_CONFIRM_DELETE)) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> currencyConversionFactorService.delete(monitor, selectedCurrencyConversionFactor.getId()),
					this::reloadTables
			);
		}
	}
	
	private void createOrEditCurrencyConversionFactor(DtoCurrencyConversionFactor input) {
		Optional<DtoCurrencyConversionFactor> dtoCurrencyConversionFactor = CustomDialogBuilder.getInstance()
				.titleKey(MessageKey.CURRENCY_CONVERSION_FACTOR_EDIT_TITLE)
				.addButton(MessageKey.CURRENCY_CONVERSION_FACTOR_EDIT_CANCEL, ButtonData.NO)
				.addButton(MessageKey.CURRENCY_CONVERSION_FACTOR_EDIT_SAVE, ButtonData.OK_DONE)
				.withFxmlContent(FxmlKey.CURRENCY_CONVERSION_FACTOR_EDIT)
				.withMapper(currencyConversionFactorEditDialogMapper)
				.withInput(input)
				.build()
				.showAndWait();
		
		if (dtoCurrencyConversionFactor.isPresent()) {
			asyncProcessor.runAsyncVoid(
					() -> {},
					monitor -> currencyConversionFactorService.saveOrUpdate(monitor, dtoCurrencyConversionFactor.get()),
					this::reloadTables
			);
		}
	}
	
	private void reloadTables() {
		tableView.reload();
		
		ExtendedTable<?, ?> depotDeliveriesTableView = (ExtendedTable<?, ?>)MainController.mainWindow.getScene().getRoot().lookup("#depotDeliveriesTableView");
		if (depotDeliveriesTableView != null) {
			depotDeliveriesTableView.reload();
		}
	}
}
