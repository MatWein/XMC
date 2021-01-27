package org.xmc.fe.stages.main.administration.ccf;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.ccf.CurrencyConversionFactorService;
import org.xmc.common.stubs.ccf.CurrencyConversionFactorOverviewFields;
import org.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.administration.ccf.mapper.CurrencyConversionFactorEditDialogMapper;
import org.xmc.fe.ui.CustomDialogBuilder;
import org.xmc.fe.ui.DialogHelper;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.table.ExtendedTable;

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
					() -> tableView.reload()
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
					() -> tableView.reload()
			);
		}
	}
}
