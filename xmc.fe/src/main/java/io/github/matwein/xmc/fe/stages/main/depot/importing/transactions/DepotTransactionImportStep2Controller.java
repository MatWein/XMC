package io.github.matwein.xmc.fe.stages.main.depot.importing.transactions;

import io.github.matwein.xmc.common.services.imoprting.IImportTemplateService;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.importing.CsvSeparator;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportTemplate;
import io.github.matwein.xmc.common.stubs.importing.ImportTemplateType;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.importing.DtoColumnMappingFactory;
import io.github.matwein.xmc.fe.stages.main.depot.importing.transactions.populator.DepotTransactionImportStep2Populator;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IAfterInit;
import io.github.matwein.xmc.fe.ui.converter.GenericItemToStringConverter;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationComboBox;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationFileChooserField;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@FxmlController
public class DepotTransactionImportStep2Controller implements IAfterInit<DtoImportData<DepotTransactionImportColmn>> {
	private final AsyncProcessor asyncProcessor;
	private final IImportTemplateService importTemplateService;
	private final DtoColumnMappingFactory dtoColumnMappingFactory;
	private final DepotTransactionImportStep2Populator depotTransactionImportStep2Populator;
	
	private DtoImportData<DepotTransactionImportColmn> importData;
	
	@FXML private ValidationFileChooserField fileChooserField;
	@FXML private ValidationComboBox<DtoImportTemplate<DepotTransactionImportColmn>> fileTemplateComboBox;
	@FXML private RadioButton addAllRadioButton;
	@FXML private RadioButton addOnlyRadioButton;
	@FXML private RadioButton addAndUpdateExistingRadioButton;
	@FXML private ValidationComboBox<CsvSeparator> csvSeparatorComboBox;
	@FXML private ValidationComboBox encodingComboBox;
	
	@Autowired
	public DepotTransactionImportStep2Controller(
			AsyncProcessor asyncProcessor,
			IImportTemplateService importTemplateService,
			DtoColumnMappingFactory dtoColumnMappingFactory,
			DepotTransactionImportStep2Populator depotTransactionImportStep2Populator) {
		
		this.asyncProcessor = asyncProcessor;
		this.importTemplateService = importTemplateService;
		this.dtoColumnMappingFactory = dtoColumnMappingFactory;
		this.depotTransactionImportStep2Populator = depotTransactionImportStep2Populator;
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
	public void afterInitialize(DtoImportData<DepotTransactionImportColmn> importData) {
		this.importData = importData;
	}
	
	private void onSelectTemplate(DtoImportTemplate<DepotTransactionImportColmn> newValue) {
		if (newValue == null || importData == null) {
			return;
		}
		
		importData.setColmuns(newValue.getColmuns());
		importData.setCsvSeparator(newValue.getCsvSeparator());
		importData.setImportType(newValue.getImportType());
		importData.setStartWithLine(newValue.getStartWithLine());
		importData.setTemplateToSaveName(newValue.getTemplateToSaveName());
		importData.setEncoding(newValue.getEncoding());
		
		depotTransactionImportStep2Populator.populateGui(importData, this);
	}
	
	private List<DtoImportTemplate<DepotTransactionImportColmn>> loadTemplates(io.github.matwein.xmc.fe.async.AsyncMonitor monitor) {
		List<DtoImportTemplate<DepotTransactionImportColmn>> templates = importTemplateService.loadImportTemplates(
				monitor,
				ImportTemplateType.DEPOT_TRANSACTION,
				DepotTransactionImportColmn.class);
		
		for (DtoImportTemplate<DepotTransactionImportColmn> template : templates) {
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
