package org.xmc.fe.stages.main.cashaccount.importing;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.entities.importing.ImportTemplateType;
import org.xmc.be.services.importing.ImportTemplateService;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.importing.CsvSeparator;
import org.xmc.common.stubs.importing.DtoImportTemplate;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.importing.DtoColumnMappingFactory;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationFileChooserField;

import java.util.List;

@FxmlController
public class CashAccountTransactionImportStep2Controller {
	private final AsyncProcessor asyncProcessor;
	private final ImportTemplateService importTemplateService;
	private final DtoColumnMappingFactory dtoColumnMappingFactory;
	
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
			DtoColumnMappingFactory dtoColumnMappingFactory) {
		
		this.asyncProcessor = asyncProcessor;
		this.importTemplateService = importTemplateService;
		this.dtoColumnMappingFactory = dtoColumnMappingFactory;
	}
	
	@FXML
	public void initialize() {
		csvSeparatorComboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(MessageKey.IMPORTING_CSV_SEPARATOR, t)));
		fileTemplateComboBox.setConverter(GenericItemToStringConverter.getInstance(DtoImportTemplate::getTemplateToSaveName));
		
		asyncProcessor.runAsync(
				this::loadTemplates,
				result -> fileTemplateComboBox.getItems().addAll(result)
		);
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
