package org.xmc.fe.stages.main.cashaccount.importing;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.entities.importing.ImportTemplateType;
import org.xmc.be.services.importing.ImportTemplateService;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.importing.CsvSeparator;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportTemplate;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.importing.DtoColumnMappingFactory;
import org.xmc.fe.stages.main.cashaccount.importing.populator.CashAccountTransactionImportStep2Populator;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationFileChooserField;

import java.util.List;

@FxmlController
public class CashAccountTransactionImportStep2Controller implements IAfterInit<DtoImportData<CashAccountTransactionImportColmn>> {
	private final AsyncProcessor asyncProcessor;
	private final ImportTemplateService importTemplateService;
	private final DtoColumnMappingFactory dtoColumnMappingFactory;
	private final CashAccountTransactionImportStep2Populator cashAccountTransactionImportStep2Populator;
	
	private DtoImportData<CashAccountTransactionImportColmn> importData;
	
	@FXML private ValidationFileChooserField fileChooserField;
    @FXML private ValidationComboBox<DtoImportTemplate<CashAccountTransactionImportColmn>> fileTemplateComboBox;
    @FXML private RadioButton addAllRadioButton;
    @FXML private RadioButton addOnlyRadioButton;
    @FXML private RadioButton addAndUpdateExistingRadioButton;
	@FXML private ValidationComboBox<CsvSeparator> csvSeparatorComboBox;
	
	@Autowired
	public CashAccountTransactionImportStep2Controller(
			AsyncProcessor asyncProcessor,
			ImportTemplateService importTemplateService,
			DtoColumnMappingFactory dtoColumnMappingFactory,
			CashAccountTransactionImportStep2Populator cashAccountTransactionImportStep2Populator) {
		
		this.asyncProcessor = asyncProcessor;
		this.importTemplateService = importTemplateService;
		this.dtoColumnMappingFactory = dtoColumnMappingFactory;
		this.cashAccountTransactionImportStep2Populator = cashAccountTransactionImportStep2Populator;
	}
	
	@FXML
	public void initialize() {
		csvSeparatorComboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(MessageKey.IMPORTING_CSV_SEPARATOR, t)));
		
		fileTemplateComboBox.setConverter(GenericItemToStringConverter.getInstance(DtoImportTemplate::getTemplateToSaveName));
		fileTemplateComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> onSelectTemplate(newValue));
		
		asyncProcessor.runAsync(
				this::loadTemplates,
				result -> fileTemplateComboBox.getItems().addAll(result)
		);
	}
	
	@Override
	public void afterInitialize(DtoImportData<CashAccountTransactionImportColmn> importData) {
		this.importData = importData;
	}
	
	private void onSelectTemplate(DtoImportTemplate<CashAccountTransactionImportColmn> newValue) {
		if (newValue == null || importData == null) {
			return;
		}
		
		importData.setColmuns(newValue.getColmuns());
		importData.setCsvSeparator(newValue.getCsvSeparator());
		importData.setImportType(newValue.getImportType());
		importData.setStartWithLine(newValue.getStartWithLine());
		importData.setTemplateToSaveName(newValue.getTemplateToSaveName());
		
		cashAccountTransactionImportStep2Populator.populateGui(importData, this);
	}
	
	private List<DtoImportTemplate<CashAccountTransactionImportColmn>> loadTemplates(org.xmc.fe.async.AsyncMonitor monitor) {
		List<DtoImportTemplate<CashAccountTransactionImportColmn>> templates = importTemplateService.loadImportTemplates(
				monitor,
				ImportTemplateType.CASH_ACCOUNT_TRANSACTION,
				CashAccountTransactionImportColmn.class);
		
		for (DtoImportTemplate<CashAccountTransactionImportColmn> template : templates) {
			var populatedColumns = dtoColumnMappingFactory.generateMissingColumns(template.getColmuns());
			template.setColmuns(populatedColumns);
		}
		
		return templates;
	}
	
	public ValidationFileChooserField getFileChooserField() {
        return fileChooserField;
    }

    public ValidationComboBox<?> getFileTemplateComboBox() {
        return fileTemplateComboBox;
    }

    public RadioButton getAddOnlyRadioButton() {
        return addOnlyRadioButton;
    }

    public RadioButton getAddAndUpdateExistingRadioButton() {
        return addAndUpdateExistingRadioButton;
    }
	
	public ValidationComboBox<CsvSeparator> getCsvSeparatorComboBox() {
		return csvSeparatorComboBox;
	}
	
	public RadioButton getAddAllRadioButton() {
		return addAllRadioButton;
	}
}
