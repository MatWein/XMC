package io.github.matwein.xmc.fe.stages.main.cashaccount.importing;

import io.github.matwein.xmc.common.services.importing.IImportTemplateService;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.importing.CsvSeparator;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportTemplate;
import io.github.matwein.xmc.common.stubs.importing.ImportTemplateType;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.importing.DtoColumnMappingFactory;
import io.github.matwein.xmc.fe.stages.main.cashaccount.importing.populator.CashAccountTransactionImportStep2Populator;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IAfterInit;
import io.github.matwein.xmc.fe.ui.converter.GenericItemToStringConverter;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationComboBox;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationFileChooserField;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@FxmlController
public class CashAccountTransactionImportStep2Controller implements IAfterInit<DtoImportData<CashAccountTransactionImportColmn>> {
	private final AsyncProcessor asyncProcessor;
	private final IImportTemplateService importTemplateService;
	private final DtoColumnMappingFactory dtoColumnMappingFactory;
	private final CashAccountTransactionImportStep2Populator cashAccountTransactionImportStep2Populator;
	
	private DtoImportData<CashAccountTransactionImportColmn> importData;
	
	@FXML private ValidationFileChooserField fileChooserField;
    @FXML private ValidationComboBox<DtoImportTemplate<CashAccountTransactionImportColmn>> fileTemplateComboBox;
    @FXML private RadioButton addAllRadioButton;
    @FXML private RadioButton addOnlyRadioButton;
    @FXML private RadioButton addAndUpdateExistingRadioButton;
	@FXML private ValidationComboBox<CsvSeparator> csvSeparatorComboBox;
	@FXML private ValidationComboBox encodingComboBox;
	
	@Autowired
	public CashAccountTransactionImportStep2Controller(
			AsyncProcessor asyncProcessor,
			IImportTemplateService importTemplateService,
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
		importData.setEncoding(newValue.getEncoding());
		
		cashAccountTransactionImportStep2Populator.populateGui(importData, this);
	}
	
	private List<DtoImportTemplate<CashAccountTransactionImportColmn>> loadTemplates(io.github.matwein.xmc.fe.async.AsyncMonitor monitor) {
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
	
	public ValidationComboBox getEncodingComboBox() {
		return encodingComboBox;
	}
}
