package io.github.matwein.xmc.fe.stages.main.depot.importing.items.populator;

import io.github.matwein.xmc.common.stubs.depot.items.DepotItemImportColmn;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.ImportType;
import io.github.matwein.xmc.fe.stages.main.depot.importing.items.DepotItemImportStep2Controller;
import io.github.matwein.xmc.fe.ui.wizard.IWizardStepPopulator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DepotItemImportStep2Populator implements IWizardStepPopulator<DtoImportData<DepotItemImportColmn>, DepotItemImportStep2Controller> {
	@Override
	public void populateState(DtoImportData<DepotItemImportColmn> input, DepotItemImportStep2Controller controller) {
		String fileToImport = controller.getFileChooserField().getValueAsString();
		if (fileToImport != null) {
			input.setFileToImport(new File(fileToImport));
		}
		input.setCsvSeparator(controller.getCsvSeparatorComboBox().getSelectionModel().getSelectedItem());
		input.setEncoding(controller.getEncodingComboBox().getEditor().getText());
		
		if (controller.getAddAllRadioButton().isSelected()) {
			input.setImportType(ImportType.ADD_ALL);
		}
		if (controller.getAddOnlyRadioButton().isSelected()) {
			input.setImportType(ImportType.ADD_ONLY);
		}
		if (controller.getAddAndUpdateExistingRadioButton().isSelected()) {
			input.setImportType(ImportType.ADD_AND_UPDATE_EXISTING);
		}
	}
	
	@Override
	public void populateGui(DtoImportData<DepotItemImportColmn> importData, DepotItemImportStep2Controller controller) {
		if (importData.getCsvSeparator() != null) {
			controller.getCsvSeparatorComboBox().getSelectionModel().select(importData.getCsvSeparator());
		}
		
		if (StringUtils.isNotBlank(importData.getEncoding())) {
			controller.getEncodingComboBox().getSelectionModel().select(importData.getEncoding());
			controller.getEncodingComboBox().getEditor().setText(importData.getEncoding());
		}
		
		if (importData.getImportType() != null) {
			switch (importData.getImportType()) {
				case ADD_ALL:
					controller.getAddAllRadioButton().setSelected(true);
					break;
				case ADD_ONLY:
					controller.getAddOnlyRadioButton().setSelected(true);
					break;
				case ADD_AND_UPDATE_EXISTING:
					controller.getAddAndUpdateExistingRadioButton().setSelected(true);
					break;
			}
		}
	}
}
