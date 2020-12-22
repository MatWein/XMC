package org.xmc.fe.stages.main.cashaccount.importing.populator;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.ImportType;
import org.xmc.fe.stages.main.cashaccount.importing.CashAccountTransactionImportStep2Controller;
import org.xmc.fe.ui.wizard.IWizardStepPopulator;

import java.io.File;

@Component
public class CashAccountTransactionImportStep2Populator implements IWizardStepPopulator<DtoImportData<CashAccountTransactionImportColmn>, CashAccountTransactionImportStep2Controller> {
    @Override
    public void populateState(DtoImportData<CashAccountTransactionImportColmn> input, CashAccountTransactionImportStep2Controller controller) {
	    String fileToImport = controller.getFileChooserField().getValueAsString();
	    if (fileToImport != null) {
		    input.setFileToImport(new File(fileToImport));
	    }
	    input.setCsvSeparator(controller.getCsvSeparatorComboBox().getSelectionModel().getSelectedItem());
	
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
	public void populateGui(DtoImportData<CashAccountTransactionImportColmn> importData, CashAccountTransactionImportStep2Controller controller) {
    	if (importData.getCsvSeparator() != null) {
		    controller.getCsvSeparatorComboBox().getSelectionModel().select(importData.getCsvSeparator());
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
