package io.github.matwein.xmc.fe.stages.main.depot.importing.items;

import io.github.matwein.xmc.common.services.imoprting.IImportTemplateService;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemImportColmn;
import io.github.matwein.xmc.common.stubs.importing.CsvSeparator;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportTemplate;
import io.github.matwein.xmc.common.stubs.importing.ImportTemplateType;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.importing.DtoColumnMappingFactory;
import io.github.matwein.xmc.fe.stages.main.depot.importing.items.populator.DepotItemImportStep2Populator;
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
public class DepotItemImportStep2Controller implements IAfterInit<DtoImportData<DepotItemImportColmn>> {
	private final AsyncProcessor asyncProcessor;
	private final IImportTemplateService importTemplateService;
	private final DtoColumnMappingFactory dtoColumnMappingFactory;
	private final DepotItemImportStep2Populator depotItemImportStep2Populator;
	
	private DtoImportData<DepotItemImportColmn> importData;
	
	@FXML private ValidationFileChooserField fileChooserField;
	@FXML private ValidationComboBox<DtoImportTemplate<DepotItemImportColmn>> fileTemplateComboBox;
	@FXML private RadioButton addAllRadioButton;
	@FXML private RadioButton addOnlyRadioButton;
	@FXML private RadioButton addAndUpdateExistingRadioButton;
	@FXML private ValidationComboBox<CsvSeparator> csvSeparatorComboBox;
	@FXML private ValidationComboBox encodingComboBox;
	
	@Autowired
	public DepotItemImportStep2Controller(
			AsyncProcessor asyncProcessor,
			IImportTemplateService importTemplateService,
			DtoColumnMappingFactory dtoColumnMappingFactory,
			DepotItemImportStep2Populator depotItemImportStep2Populator) {
		
		this.asyncProcessor = asyncProcessor;
		this.importTemplateService = importTemplateService;
		this.dtoColumnMappingFactory = dtoColumnMappingFactory;
		this.depotItemImportStep2Populator = depotItemImportStep2Populator;
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
	public void afterInitialize(DtoImportData<DepotItemImportColmn> importData) {
		this.importData = importData;
	}
	
	private void onSelectTemplate(DtoImportTemplate<DepotItemImportColmn> newValue) {
		if (newValue == null || importData == null) {
			return;
		}
		
		importData.setColmuns(newValue.getColmuns());
		importData.setCsvSeparator(newValue.getCsvSeparator());
		importData.setImportType(newValue.getImportType());
		importData.setStartWithLine(newValue.getStartWithLine());
		importData.setTemplateToSaveName(newValue.getTemplateToSaveName());
		importData.setEncoding(newValue.getEncoding());
		
		depotItemImportStep2Populator.populateGui(importData, this);
	}
	
	private List<DtoImportTemplate<DepotItemImportColmn>> loadTemplates(io.github.matwein.xmc.fe.async.AsyncMonitor monitor) {
		List<DtoImportTemplate<DepotItemImportColmn>> templates = importTemplateService.loadImportTemplates(
				monitor,
				ImportTemplateType.DEPOT_ITEM,
				DepotItemImportColmn.class);
		
		for (DtoImportTemplate<DepotItemImportColmn> template : templates) {
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
